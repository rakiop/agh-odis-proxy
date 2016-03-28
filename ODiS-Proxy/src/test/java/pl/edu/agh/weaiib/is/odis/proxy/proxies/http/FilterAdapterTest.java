package pl.edu.agh.weaiib.is.odis.proxy.proxies.http;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.AttributeKey;
import org.junit.Test;
import org.littleshoot.proxy.HttpFilters;
import org.littleshoot.proxy.HttpFiltersAdapter;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.ODiSHttpFilter;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.ProxyServer;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.LinkedList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class FilterAdapterTest {

    @Test
    public void getOnlyHttpResponseFilters(){
        ProxyServer server = mock(ProxyServer.class);
        HttpRequest request = mock(HttpRequest.class);
        ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);
        Channel channel = mock(Channel.class);


        when(server.getFilters(FilterPlace.SERVER_CLIENT_TO_PROXY)).thenReturn(new LinkedList<>());
        when(server.getAddress()).thenReturn(null);
        when(request.getMethod()).thenReturn(null);
        when(request.getUri()).thenReturn(null);
        when(ctx.channel()).thenReturn(channel);
        when(channel.remoteAddress()).thenReturn(null);

        FilterAdapter adapter = new FilterAdapter(server);
        adapter.filterRequest(request, ctx);

        verify(server, times(1)).getFilters(any());
        verify(server, times(1)).getFilters(FilterPlace.SERVER_CLIENT_TO_PROXY);
    }

    @Test
    public void runOnlyTestHttpRequest(){
        ProxyServer server = mock(ProxyServer.class);
        HttpRequest request = mock(HttpRequest.class);
        ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);
        Channel channel = mock(Channel.class);
        ODiSHttpFilter filter = mock(ODiSHttpFilter.class);

        when(server.getFilters(FilterPlace.SERVER_CLIENT_TO_PROXY)).thenReturn(Arrays.asList(filter));
        when(server.getAddress()).thenReturn(null);
        when(request.getMethod()).thenReturn(null);
        when(request.getUri()).thenReturn(null);
        when(ctx.channel()).thenReturn(channel);
        when(channel.remoteAddress()).thenReturn(null);
        when(filter.testHttpRequest(any(), any())).thenReturn(true);

        FilterAdapter adapter = new FilterAdapter(server);
        adapter.filterRequest(request, ctx);

        verify(filter, times(1)).testHttpRequest(any(), any());
        verify(filter, times(0)).testHttpResponse(any(), any(), any());
    }

    @Test
    public void breakIfFilteringFails(){
        ProxyServer server = mock(ProxyServer.class);
        HttpRequest request = mock(HttpRequest.class);
        ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);
        Channel channel = mock(Channel.class);
        ODiSHttpFilter filter = mock(ODiSHttpFilter.class);

        when(server.getFilters(FilterPlace.SERVER_CLIENT_TO_PROXY)).thenReturn(Arrays.asList(filter));
        when(server.getAddress()).thenReturn(null);
        when(request.getMethod()).thenReturn(null);
        when(request.getUri()).thenReturn(null);
        when(ctx.channel()).thenReturn(channel);
        when(channel.remoteAddress()).thenReturn(null);
        when(filter.testHttpRequest(any(), any())).thenReturn(false);

        FilterAdapter adapter = new FilterAdapter(server);
        HttpFilters ret = adapter.filterRequest(request, ctx);

        assertTrue(ret instanceof OdisHttpAbortFilterAdapter);
    }

    @Test
    public void continueIfFiltersPass(){
        ProxyServer server = mock(ProxyServer.class);
        HttpRequest request = mock(HttpRequest.class);
        ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);
        Channel channel = mock(Channel.class);
        ODiSHttpFilter filter = mock(ODiSHttpFilter.class);

        when(server.getFilters(FilterPlace.SERVER_CLIENT_TO_PROXY)).thenReturn(Arrays.asList(filter));
        when(server.getAddress()).thenReturn(null);
        when(request.getMethod()).thenReturn(null);
        when(request.getUri()).thenReturn(null);
        when(ctx.channel()).thenReturn(channel);
        when(channel.remoteAddress()).thenReturn(null);
        when(filter.testHttpRequest(any(), any())).thenReturn(true);

        FilterAdapter adapter = new FilterAdapter(server);
        HttpFilters ret = adapter.filterRequest(request, ctx);

        assertTrue(ret instanceof OdisHttpFilterAdapter);
    }

    @Test
    public void sslUsesDefaultHttpFilter(){
        ProxyServer server = mock(ProxyServer.class);
        HttpRequest request = mock(HttpRequest.class);
        Channel channel = mock(Channel.class);
        ODiSHttpFilter filter = mock(ODiSHttpFilter.class);

        when(server.getFilters(FilterPlace.SERVER_CLIENT_TO_PROXY)).thenReturn(Arrays.asList(filter));
        when(server.getAddress()).thenReturn(null);
        when(request.getMethod()).thenReturn(HttpMethod.CONNECT);
        when(request.getUri()).thenReturn(null);
        when(channel.remoteAddress()).thenReturn(null);
        when(filter.testHttpRequest(any(), any())).thenReturn(true);

        FilterAdapter adapter = new FilterAdapter(server);
        HttpFilters ret = adapter.filterRequest(request, null);

        assertTrue(ret instanceof HttpFiltersAdapter);
    }

    @Test
    public void sslWritesDestinationUrlToContext(){
        String uri = "google.pl:443";

        io.netty.util.Attribute<String> attribute = getNettyAttribute(FilterAdapter.CONNECTED_URL);

        ProxyServer server = mock(ProxyServer.class);
        HttpRequest request = mock(HttpRequest.class);
        ChannelHandlerContext ctx = mock(ChannelHandlerContext.class);
        Channel channel = mock(Channel.class);
        ODiSHttpFilter filter = mock(ODiSHttpFilter.class);

        when(server.getFilters(FilterPlace.SERVER_CLIENT_TO_PROXY)).thenReturn(Arrays.asList(filter));
        when(server.getAddress()).thenReturn(null);
        when(request.getMethod()).thenReturn(HttpMethod.CONNECT);
        when(request.getUri()).thenReturn(uri);
        when(ctx.channel()).thenReturn(channel);
        when(channel.remoteAddress()).thenReturn(new InetSocketAddress(20));
        when(channel.attr(FilterAdapter.CONNECTED_URL)).thenReturn(attribute);
        when(filter.testHttpRequest(any(), any())).thenReturn(true);

        FilterAdapter adapter = new FilterAdapter(server);
        adapter.filterRequest(request, ctx);

        verify(channel, times(1)).attr(FilterAdapter.CONNECTED_URL);
        assertEquals("https://google.pl", attribute.get());
    }

    @Test
    public void bufferMoreThen0ToAggregate(){
        FilterAdapter adapter = new FilterAdapter(null);
        assertTrue(adapter.getMaximumRequestBufferSizeInBytes() > 0);
        assertTrue(adapter.getMaximumResponseBufferSizeInBytes() > 0);
    }

    io.netty.util.Attribute<String> getNettyAttribute(AttributeKey<String> key){
        return new io.netty.util.Attribute<String>() {
            private String value = "";

            @Override
            public AttributeKey<String> key() {
                return key;
            }

            @Override
            public String get() {
                return value;
            }

            @Override
            public void set(String value) {
                this.value = value;
            }

            @Override
            public String getAndSet(String value) {
                this.value = value;
                return value;
            }

            @Override
            public String setIfAbsent(String value) {
                this.value = value;
                return value;
            }

            @Override
            public String getAndRemove() {
                return value;
            }

            @Override
            public boolean compareAndSet(String oldValue, String newValue) {
                boolean equals = oldValue.equals(newValue);
                if(!equals)
                    value = newValue;
                return equals;
            }

            @Override
            public void remove() {

            }
        };
    }

}
