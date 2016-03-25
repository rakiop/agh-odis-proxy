package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.weaiib.is.odis.proxy.helpers.URIHelper;

public class BlackListUrlsPlugin extends ODiSHttpFilter{

    private static final Logger LOGGER = LoggerFactory.getLogger(BlackListUrlsPlugin.class);

    private List<String> urls = new ArrayList<String>();

    @Override
    public void init() {
        String propertyList = parameters.get("list");
        if(propertyList != null){
            String[] urlsToTrim = propertyList.split(";");
            for(String url : urlsToTrim){
                String trimmedUrl = url.trim().toLowerCase();
                if(trimmedUrl.startsWith("www."))
                    trimmedUrl = trimmedUrl.substring(4);
                if(!trimmedUrl.isEmpty() && !urls.contains(trimmedUrl)){
                    urls.add(trimmedUrl);
                }
            }
        }
    }

    @Override
    public boolean testHttpRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        String url = originalRequest.getUri();
        try {
            URI uri = URIHelper.getURIFromUrl(url);
            String host = uri.getHost();
            if(host.startsWith("www."))
                host = host.substring(4);

            try {
                InetAddress addr = InetAddress.getByName(host);
                host = addr.getHostName();
            } catch (UnknownHostException e) {
                LOGGER.warn(String.format("Could not resolve host with InetAddress: %s", e.getMessage()));
            }

            return !urls.contains(host);
        } catch (URISyntaxException e) {
            LOGGER.warn(String.format("Could not resolve host to URI: %s", e.getMessage()));
        }

        return true;
    }

    @Override
    public boolean testHttpResponse(HttpRequest originalRequest, HttpObject response, String content) {
        return true;
    }
}
