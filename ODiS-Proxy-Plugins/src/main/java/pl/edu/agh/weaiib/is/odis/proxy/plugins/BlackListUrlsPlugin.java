package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.weaiib.is.odis.proxy.SerializableList;
import pl.edu.agh.weaiib.is.odis.proxy.helpers.URIHelper;

import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Plugin to blacklist specific domains
 */
public class BlackListUrlsPlugin extends ODiSHttpFilter{

    /**
     * Main application logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BlackListUrlsPlugin.class);

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
     * List to validate full domain address
     */
    private final List<String> fullDomains = new ArrayList<>();

    /**
     * List to validate if current url is subdomain of
     * blacklisted domain
     */
    private final List<String> subdomainsSufix = new ArrayList<>();

    /**
     * Initialization of blacklists entries from
     * {@code parameters} with key "list"
     */
    @Override
    public void init() {
        Object propertyListObject = parameters.get("list");
        if(propertyListObject instanceof SerializableList){
            SerializableList propertyList = (SerializableList)propertyListObject;
            for(String url : propertyList.getList()){
                String trimmedUrl = url.trim().toLowerCase();
                if(trimmedUrl.startsWith("www."))
                    trimmedUrl = trimmedUrl.substring(4);
                if(!trimmedUrl.isEmpty() && !fullDomains.contains(trimmedUrl)){
                    fullDomains.add(trimmedUrl);
                    subdomainsSufix.add("." + trimmedUrl);
                }
            }
        }
    }

    /**
     * Test if current http request is to blacklisted domain
     * @param originalRequest   Http request
     * @param ctx               Context
     * @return                  Whether domain is good or not
     */
    @Override
    public FilterResponse testHttpRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        String uri = originalRequest.getUri();
        String host = getHost(uri);

        if(fullDomains.contains(host))
            return new FilterResponse(false, "This domain is on black list");

        for(String sub : subdomainsSufix){
            if(host.endsWith(sub))
                return new FilterResponse(false, "This domain is on black list");
        }

        return new FilterResponse(true);
    }

    /**
     * Get host from full request url
     * @param url   Request URL
     * @return      Host of URL
     */
    private String getHost(String url){
        String host = "";
        try {
            URI uri = URIHelper.getURIFromUrl(url);
            host = uri.getHost();
            if(host == null) {
                host = uri.getScheme();
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

    /**
     * Testing response is disabled - always return {@code true}
     * @param originalRequest   Request
     * @param response          Response
     * @param content           Parsed content
     * @return                  Always {@code true}
     */
    @Override
    public FilterResponse testHttpResponse(HttpRequest originalRequest, HttpObject response, String content) {
        return new FilterResponse(true);
    }
}
