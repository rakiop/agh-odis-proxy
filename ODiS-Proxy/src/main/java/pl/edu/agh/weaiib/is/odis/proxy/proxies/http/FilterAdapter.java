package pl.edu.agh.weaiib.is.odis.proxy.proxies.http;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.AttributeKey;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.littleshoot.proxy.HttpFiltersSourceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.ODiSHttpFilter;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.ProxyServer;

import java.util.List;

/**
 * Adapter to handle HTTP requests
 */
public class FilterAdapter extends HttpFiltersSourceAdapter {

    /**
     * Main application logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(FilterAdapter.class);

    /**
     * Proxy server handler
     */
    private final ProxyServer server;

    /**
     * Attribute to save request url for ssl connection
     */
    public static final AttributeKey<String> CONNECTED_URL = AttributeKey.valueOf("connected_url");

    /**
     * Default constructor
     * @param server    Proxy server handler
     */
    public FilterAdapter(ProxyServer server) {
        this.server = server;
    }

    /**
     * Handle HTTP request, do pre-filtering, save request url in SSL connection
     * and return proper HtppFilters class
     * @param originalRequest   request
     * @param ctx               context
     * @return                  HttpFilter class to do post-filtering
     */
    public HttpFilters filterRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        String from = ctx == null || ctx.channel().remoteAddress() == null
                ? "" : ctx.channel().remoteAddress().toString();
        LOGGER.info(String.format("[%s] New request from %s to host: [%s] %s", server.getAddress(), from, originalRequest.getMethod(), originalRequest.getUri()));

        List<Filter> filters = server.getFilters(FilterPlace.SERVER_CLIENT_TO_PROXY);
        boolean canContinue = true;
        for (Filter filter : filters) {
            canContinue &= ((ODiSHttpFilter)filter).testHttpRequest(originalRequest, ctx);
            if (!canContinue) {
                LOGGER.info(String.format("Request stopped by %s", filter.getClass().getName()));
                break;
            }
        }

        // If false then break connection
        if (!canContinue) {
            return new OdisHttpAbortFilterAdapter(originalRequest, ctx);
        }

        if (originalRequest.getMethod() == HttpMethod.CONNECT) {
            if (ctx != null) {
                String uri = originalRequest.getUri();
                String prefix = "https://" + uri.replaceFirst(":443", "");
                ctx.channel().attr(CONNECTED_URL).set(prefix);
            }
            return new HttpFiltersAdapter(originalRequest, ctx);
        }

        return new OdisHttpFilterAdapter(originalRequest, ctx, server);
    }

    /**
     * Return buffer size - more then 0 to aggregate batches
     * @return  more than 0 value
     */
    @Override
    public int getMaximumResponseBufferSizeInBytes() {
        return 10 * 1024 * 1024;
    }

    /**
     * Return buffer size - more then 0 to aggregate batches
     * @return  more than 0 value
     */
    @Override
    public int getMaximumRequestBufferSizeInBytes() {
        return 10 * 1024 * 1024;
    }
}
