package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import io.netty.handler.codec.http.HttpRequest;
import org.junit.Test;
import pl.edu.agh.weaiib.is.odis.proxy.SerializableList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MyWOTPluginTest {

    @Test
    public void testHttpResponseReturnTrue(){
        MyWOTPlugin myWOTPlugin = getMock(60, false);
        FilterResponse response = myWOTPlugin.testHttpResponse(null, null, null);

        assert(response.getStatus());
    }

    @Test
    public void googleIsTrusted(){
        HttpRequest request = mock(HttpRequest.class);
        when(request.getUri()).thenReturn("http://google.com");

        MyWOTPlugin myWOTPlugin = getMock(60, false);
        FilterResponse response = myWOTPlugin.testHttpRequest(request, null);

        assert(response.getStatus());
    }

    @Test
    public void pornhubIsNotTrusted(){
        HttpRequest request = mock(HttpRequest.class);
        when(request.getUri()).thenReturn("http://pornhub.com");

        MyWOTPlugin myWOTPlugin = getMock(60, false, "401");
        FilterResponse response = myWOTPlugin.testHttpRequest(request, null);

        assert(!response.getStatus());
    }

    private MyWOTPlugin getMock(int trustLevel, boolean blockIfNotFound, String... blockCategories){
        MyWOTPlugin plugin = new MyWOTPlugin();
        Map<String, Object> params = new HashMap<>();
        params.put("trustLevel", trustLevel);
        params.put("blockIfNotFound", blockIfNotFound);
        if(blockCategories != null && blockCategories.length > 0){
            params.put("blockCategories",new SerializableList(Arrays.asList(blockCategories)));
        }
        plugin.setParameters(params);
        return plugin;
    }

}
