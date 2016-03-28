package pl.edu.agh.weaiib.is.odis.proxy.proxies.http;

import io.netty.buffer.DefaultByteBufHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import org.littleshoot.proxy.HttpFiltersAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.helpers.HttpResponseHelper;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.ODiSHttpFilter;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.ProxyServer;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

/**
 * Default proxy adapter
 */
public class OdisHttpFilterAdapter extends HttpFiltersAdapter {

    /**
     * Main application logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(OdisHttpFilterAdapter.class);

    /**
     * List of content types to do post - filtering
     */
    private static final List<String> filterableContentTypes = Arrays.asList(new String[]{"text/html","text/plain","application/javascript"}) ;

    /**
     * Proxy server
     */
    private ProxyServer server;

    /**
     * Request object
     */
    HttpRequest originalRequest;

    /**
     * Context object
     */
    ChannelHandlerContext ctx;

    /**
     * Default constructor
     * @param originalRequest   Request
     * @param ctx               Context
     * @param server            Proxy server
     */
    public OdisHttpFilterAdapter(HttpRequest originalRequest, ChannelHandlerContext ctx, ProxyServer server) {
        super(originalRequest, ctx);
        this.server = server;
        this.originalRequest = originalRequest;
        this.ctx = ctx;
    }

    /**
     * Filtering when proxy gets response from remote host
     * @param httpObject    Response
     * @return              Response after filtering
     */
    @Override
    public HttpObject proxyToClientResponse(HttpObject httpObject){

        if(httpObject instanceof DefaultByteBufHolder &&
            filterableContentTypes.contains(HttpResponseHelper.getContentType(httpObject))){
            DefaultByteBufHolder response = (DefaultByteBufHolder) httpObject;

            String charset = HttpResponseHelper.getCharset(httpObject);

            String content = "";
            try {
                content = new String(response.retain().content().nioBuffer().array(), charset);
            } catch (UnsupportedEncodingException e) {
                LOGGER.warn(String.format("Content cannot be filtered [Charset issue {%s}]: %s", charset, e.getMessage()));
            } catch (UnsupportedOperationException e) {
                LOGGER.warn("Content cannot be filtered: No accessible array");
            }

            if(content.isEmpty()){
                return httpObject;
            }

            List<Filter> filters = server.getFilters(FilterPlace.SERVER_PROXY_TO_CLIENT);
            boolean canContinue = true;
            for (Filter filter : filters) {
                canContinue &= ((ODiSHttpFilter)filter).testHttpResponse(originalRequest, httpObject, content);
                if (!canContinue) {
                    LOGGER.info(String.format("Response stopped by %s", filter.getClass().getName()));
                    break;
                }
            }

            if(!canContinue)
                return OdisHttpAbortFilterAdapter.getForbiddenResponse();

            try {
                byte[] contentBytes = content.getBytes(charset);
                response.retain().content().clear().writeBytes(contentBytes);
                HttpResponseHelper.updateContentLength((HttpObject)response, contentBytes.length);
            } catch (UnsupportedEncodingException e) {
                LOGGER.warn(String.format("Could not replace content: %s", e.getMessage()));
                return OdisHttpAbortFilterAdapter.getForbiddenResponse();
            }
            return (HttpObject) response;
        }

        return httpObject;
    }

}
