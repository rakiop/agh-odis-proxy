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
import pl.edu.agh.weaiib.is.odis.proxy.plugins.FilterResponse;

/**
 * Adapter to handle request / response that will be dropped
 */
public class OdisHttpAbortFilterAdapter extends HttpFiltersAdapter {

    private final FilterResponse filterResponse;

    /**
     * Default constructor
     * @param originalRequest       request
     * @param ctx                   context
     */
    public OdisHttpAbortFilterAdapter(HttpRequest originalRequest, ChannelHandlerContext ctx, FilterResponse filterResponse) {
        super(originalRequest, ctx);
        this.filterResponse = filterResponse;
    }

    /**
     * on client to proxy request return forbidden response
     * @param httpRequest       request
     * @return                  response with forbidden message
     */
    @Override
    public HttpResponse clientToProxyRequest(HttpObject httpRequest){
        if(filterResponse == null)
            return getForbiddenResponse();
        return getForbiddenResponse(filterResponse);
    }

    /**
     * Get Forbidden response
     * @return  Forbidden response
     */
    public static HttpResponse getForbiddenResponse(FilterResponse filterResponse){
        return responseFor(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, "This page is forbidden by your proxy settings.<br>" + filterResponse.getMessage());
    }

    /**
     * Get Forbidden response
     * @return  Forbidden response
     */
    public static HttpResponse getForbiddenResponse(){
        return responseFor(HttpVersion.HTTP_1_1, HttpResponseStatus.FORBIDDEN, "This page is forbidden by your proxy settings.");
    }

    /**
     * Generate default response
     * @param httpVersion   Http version
     * @param status        Response status
     * @param body          Body content
     * @return              Response
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
        DefaultFullHttpResponse response = body != null?new DefaultFullHttpResponse(httpVersion, status, body):new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        if(body != null) {
            response.headers().set("Content-Length", Integer.valueOf(contentLength));
            response.headers().set("Content-Type", "text/html; charset=UTF-8");
        }

        return response;
    }

}
