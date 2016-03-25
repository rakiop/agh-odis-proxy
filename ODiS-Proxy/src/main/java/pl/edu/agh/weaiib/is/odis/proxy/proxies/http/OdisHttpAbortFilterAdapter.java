package pl.edu.agh.weaiib.is.odis.proxy.proxies.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.nio.charset.Charset;
import org.littleshoot.proxy.HttpFiltersAdapter;

/**
 * Created by SG0222582 on 3/23/2016.
 */
public class OdisHttpAbortFilterAdapter extends HttpFiltersAdapter {
    public OdisHttpAbortFilterAdapter(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        super(originalRequest, ctx);
    }

    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpRequest){
        return getForbiddenResponse();
    }

    public static HttpResponse getForbiddenResponse(){
        return responseFor(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, "This page is forbidden by your proxy settings.");
    }

    private static DefaultFullHttpResponse responseFor(HttpVersion httpVersion, HttpResponseStatus status, String body) {
        byte[] bytes = body.getBytes(Charset.forName("UTF-8"));
        ByteBuf content = Unpooled.copiedBuffer(bytes);
        return responseFor(httpVersion, status, content, bytes.length);
    }

    private static DefaultFullHttpResponse responseFor(HttpVersion httpVersion, HttpResponseStatus status, ByteBuf body, int contentLength) {
        DefaultFullHttpResponse response = body != null?new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, body):new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        if(body != null) {
            response.headers().set("Content-Length", Integer.valueOf(contentLength));
            response.headers().set("Content-Type", "text/html; charset=UTF-8");
        }

        return response;
    }

}
