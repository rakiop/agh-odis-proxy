package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;

public abstract class ODiSHttpFilter extends Filter{

    public abstract boolean testHttpRequest(HttpRequest originalRequest, ChannelHandlerContext ctx);

    public abstract boolean testHttpResponse(HttpRequest originalRequest, HttpObject response, String content);
}
