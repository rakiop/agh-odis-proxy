package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.weaiib.is.odis.proxy.SerializableList;
import pl.edu.agh.weaiib.is.odis.proxy.helpers.URIHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Plugin to blacklist specific domains
 */
public class BlackListUrlsPlugin extends ODiSHttpFilter{

    /**
     * Main application logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(BlackListUrlsPlugin.class);

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
        String host = URIHelper.getHost(uri);

        if(fullDomains.contains(host))
            return new FilterResponse(false, "This domain is on black list");

        for(String sub : subdomainsSufix){
            if(host.endsWith(sub))
                return new FilterResponse(false, "This domain is on black list");
        }

        return new FilterResponse(true);
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
