package pl.edu.agh.weaiib.is.odis.proxy.helpers;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Get valid URI object from string URI
 */
public class URIHelper {

    /**
     * Gets valid URI object from request url
     * @param url                   Request URL
     * @return                      Valid URI object
     * @throws URISyntaxException   If conversion fails
     */
    public static URI getURIFromUrl(String url) throws URISyntaxException {
        try{
            URI uri = new URI(url);
            return uri;
        }catch(URISyntaxException e){
            StringBuilder resultStr = new StringBuilder();
            int doubleBackslash = url.indexOf("://")+3;
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
        if (ch > 128 || ch < 0)
            return true;
        return " %$+,:;@<>#%{}[]'\"".indexOf(ch) >= 0;
    }

}
