package pl.edu.agh.weaiib.is.odis.proxy.proxies.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import org.littleshoot.proxy.HttpFiltersAdapter;

import java.nio.charset.Charset;

/**
 * Adapter to handle request / response that will be dropped
 */
public class OdisHttpAbortFilterAdapter extends HttpFiltersAdapter {

    /**
     * Default constructor
     * @param originalRequest       request
     * @param ctx                   context
     */
    public OdisHttpAbortFilterAdapter(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        super(originalRequest, ctx);
    }

    /**
     * on client to proxy request return forbidden response
     * @param httpRequest       request
     * @return                  response with forbidden message
     */
    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpRequest){
        return getForbiddenResponse();
    }

    /**
     * Get Forbidden response
     * @return  Forbidden response
     */
    public static HttpResponse getForbiddenResponse(){
        return responseFor(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, "This page is forbidden by your proxy settings.");
    }

    /**
     *
     * @param httpVersion
     * @param status
     * @param body
     * @return
     */
    private static DefaultFullHttpResponse responseFor(HttpVersion httpVersion, HttpResponseStatus status, String body) {
        byte[] bytes = body.getBytes(Charset.forName("UTF-8"));
        ByteBuf content = Unpooled.copiedBuffer(bytes);
        return responseFor(httpVersion, status, content, bytes.length);
    }

    /**
     * Response builder
     * @param httpVersion       Http version
     * @param status            Response status
     * @param body              Content
     * @param contentLength     Content length
     * @return                  Response object
     */
    private static DefaultFullHttpResponse responseFor(HttpVersion httpVersion, HttpResponseStatus status, ByteBuf body, int contentLength) {
        DefaultFullHttpResponse response = body != null?new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, body):new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        if(body != null) {
            response.headers().set("Content-Length", Integer.valueOf(contentLength));
            response.headers().set("Content-Type", "text/html; charset=UTF-8");
        }

        return response;
    }

}
