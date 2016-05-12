package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

/**
 * Interface for http filters / plugins
 */
public abstract class ODiSHttpFilter extends Filter{

    /**
     * Method to filter request when new request is created
     * @param originalRequest   Request
     * @param ctx               Contest
     * @return                  Pass this request forward or drop it
     */
    public abstract FilterResponse testHttpRequest(HttpRequest originalRequest, ChannelHandlerContext ctx);

    /**
     * Filter response content when proxy gets it from remote host
     * @param originalRequest   Request object
     * @param response          Response object - aggregated
     * @param content           Parsed content to text format
     * @return                  Pass this response or drop it
     */
    public abstract FilterResponse testHttpResponse(HttpRequest originalRequest, HttpObject response, String content);
}
