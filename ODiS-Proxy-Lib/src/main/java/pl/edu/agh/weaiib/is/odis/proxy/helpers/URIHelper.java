package pl.edu.agh.weaiib.is.odis.proxy.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * Get valid URI object from string URI
 */
public class URIHelper {

    /**
     * Main application logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(URIHelper.class);

    /**
     * Pattern to check  if url is IPv4 based
     */
    private static final Pattern IPv4Pattern = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");

    /**
     * Pattern to check if url is IPv6 split by colon based
     */
    private static final Pattern IPv6Patternv1 = Pattern.compile("[0-9a-fA-F]{1,4}:[0-9a-fA-F]{1,4}:[0-9a-fA-F]{1,4}:[0-9a-fA-F]{1,4}:[0-9a-fA-F]{1,4}:[0-9a-fA-F]{1,4}:[0-9a-fA-F]{1,4}:[0-9a-fA-F]{1,4}");

    /**
     * pattern to check if url is IPv6 with dots based
     */
    private static final Pattern IPv6Patternv2 = Pattern.compile("[0-9a-fA-F]{1,4}\\.[0-9a-fA-F]{1,4}\\.[0-9a-fA-F]{1,4}\\.[0-9a-fA-F]{1,4}\\.[0-9a-fA-F]{1,4}\\.[0-9a-fA-F]{1,4}\\.[0-9a-fA-F]{1,4}\\.[0-9a-fA-F]{1,4}");

    /**
     * Gets valid URI object from request url
     * @param url                   Request URL
     * @return                      Valid URI object
     * @throws URISyntaxException   If conversion fails
     */
    public static URI getURIFromUrl(String url) throws URISyntaxException {
        try{
            return new URI(url);
        }catch(URISyntaxException e){
            StringBuilder resultStr = new StringBuilder();
            int doubleBackslash = url.indexOf("://")+3;
            if(doubleBackslash == 2) doubleBackslash = 0;
            resultStr.append(url.substring(0, doubleBackslash));
            String restOfUrl = url.substring(doubleBackslash);
            for (char ch : restOfUrl.toCharArray()) {
                if (isUnsafe(ch)) {
                    resultStr.append('%');
                    resultStr.append(toHex(ch / 16));
                    resultStr.append(toHex(ch % 16));
                } else {
                    resultStr.append(ch);
                }
            }
            return new URI(resultStr.toString());
        }
    }

    /**
     * Char value of mark
     * @param ch    mark unicode number part
     * @return      char value
     */
    private static char toHex(int ch) {
        return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
    }

    /**
     * Check if mark should be unicoded
     * @param ch    mark
     * @return      Is mark not valid?
     */
    private static boolean isUnsafe(char ch) {
        return ch > 128 || ch < 0 || " %$+,:;@<>#%{}[]'\"".indexOf(ch) >= 0;
    }

    /**
     * Get host from full request url
     * @param url   Request URL
     * @return      Host of URL
     */
    public static String getHost(String url){
        String host = "";
        try {
            URI uri = URIHelper.getURIFromUrl(url);
            host = uri.getHost();
            if(host == null) {
                host = uri.getAuthority();
            }
            if(host == null){
                host = uri.getScheme();
            }

            if(host == null){
                return "";
            }

            if(host.startsWith("www."))
                host = host.substring(4);

            try {
                if(IPv4Pattern.matcher(host).find() ||
                        IPv6Patternv1.matcher(host).find() ||
                        IPv6Patternv2.matcher(host).find()){
                    InetAddress inetAddress = InetAddress.getByName(host);
                    host = inetAddress.getHostName();
                }
            } catch (UnknownHostException e) {
                LOGGER.warn(String.format("Could not resolve host with InetAddress: %s", e.getMessage()));
            }

            return host;
        } catch (URISyntaxException e) {
            LOGGER.warn(String.format("Could not resolve host to URI: %s", e.getMessage()));
        }
        return host;
    }


}
