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

public class BlackListUrlsPlugin extends ODiSHttpFilter{

    private static final Logger LOGGER = LoggerFactory.getLogger(BlackListUrlsPlugin.class);

    private static final Pattern IPv4Pattern = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
    private static final Pattern IPv6Patternv1 = Pattern.compile("[0-9a-fA-F]{1,4}:[0-9a-fA-F]{1,4}:[0-9a-fA-F]{1,4}:[0-9a-fA-F]{1,4}:[0-9a-fA-F]{1,4}:[0-9a-fA-F]{1,4}:[0-9a-fA-F]{1,4}:[0-9a-fA-F]{1,4}");
    private static final Pattern IPv6Patternv2 = Pattern.compile("[0-9a-fA-F]{1,4}\\.[0-9a-fA-F]{1,4}\\.[0-9a-fA-F]{1,4}\\.[0-9a-fA-F]{1,4}\\.[0-9a-fA-F]{1,4}\\.[0-9a-fA-F]{1,4}\\.[0-9a-fA-F]{1,4}\\.[0-9a-fA-F]{1,4}");

    private List<String> fullDomains = new ArrayList<String>();
    private List<String> subdomainsSufix = new ArrayList<String>();

    @Override
    public void init() {
        Object propertyListObject = parameters.get("list");
        if(propertyListObject instanceof SerializableList){
            SerializableList<String> propertyList = (SerializableList<String>)propertyListObject;
            if(propertyList != null){
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
    }

    @Override
    public boolean testHttpRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        String uri = originalRequest.getUri();
        String host = getHost(uri);

        if(fullDomains.contains(host))
            return false;

        for(String sub : subdomainsSufix){
            if(host.endsWith(sub))
                return false;
        }

        return true;
    }

    private String getHost(String url){
        String host = "";
        try {
            URI uri = URIHelper.getURIFromUrl(url);
            host = uri.getHost();
            if(host == null)
                host = uri.getSchemeSpecificPart();
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

    @Override
    public boolean testHttpResponse(HttpRequest originalRequest, HttpObject response, String content) {
        return true;
    }
}
