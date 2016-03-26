package pl.edu.agh.weaiib.is.odis.proxy.plugins;


import io.netty.handler.codec.http.HttpRequest;
import org.junit.Test;
import pl.edu.agh.weaiib.is.odis.proxy.SerializableList;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BlackListUrlsPluginTest {

    @Test
    public void dropDomainIfOnList(){
        BlackListUrlsPlugin list = initializeBlackListUrls("google.pl");
        HttpRequest request = mock(HttpRequest.class);
        when(request.getUri()).thenReturn("http://google.pl/?q=question");

        assertFalse(list.testHttpRequest(request, null));
    }

    @Test
    public void passDomainIfNotOnList(){
        BlackListUrlsPlugin list = initializeBlackListUrls("google.pl");
        HttpRequest request = mock(HttpRequest.class);
        when(request.getUri()).thenReturn("http://google.com/?q=question");

        assertTrue(list.testHttpRequest(request, null));
    }

    @Test
    public void dropDomainIfUrlIsIpAndDomainOnList(){
        BlackListUrlsPlugin list = initializeBlackListUrls("coachingsport.pl");
        HttpRequest request = mock(HttpRequest.class);
        when(request.getUri()).thenReturn("http://5.133.13.87");

        assertFalse(list.testHttpRequest(request, null));
    }

    @Test
    public void dropIfIsSubdomain(){
        BlackListUrlsPlugin list = initializeBlackListUrls("kwejk.pl");
        HttpRequest request = mock(HttpRequest.class);
        when(request.getUri()).thenReturn("http://m.kwejk.pl");

        assertFalse(list.testHttpRequest(request, null));
    }

    @Test
    public void dropWWWPrefixFromBlakListWorks(){
        BlackListUrlsPlugin list = initializeBlackListUrls("www.google.pl");
        HttpRequest request = mock(HttpRequest.class);
        when(request.getUri()).thenReturn("http://google.pl");

        assertFalse(list.testHttpRequest(request, null));
    }

    @Test
    public void dropWWWPrefixedUrl(){
        BlackListUrlsPlugin list = initializeBlackListUrls("google.pl");
        HttpRequest request = mock(HttpRequest.class);
        when(request.getUri()).thenReturn("http://www.google.pl");

        assertFalse(list.testHttpRequest(request, null));
    }


    @Test
    public void responseTestAlwaysTrue(){
        BlackListUrlsPlugin list = initializeBlackListUrls("kwejk.pl");
        assertTrue(list.testHttpResponse(null, null, null));
    }

    @Test
    public void unknowHostIpNoError(){
        BlackListUrlsPlugin list = initializeBlackListUrls("google.pl");
        HttpRequest request = mock(HttpRequest.class);
        when(request.getUri()).thenReturn("http://0000:0000:0000:0000:0000:0000:0000:0000%5");

        assertTrue(list.testHttpRequest(request, null));
    }

    @Test
    public void wrongUriProtection(){
        BlackListUrlsPlugin list = initializeBlackListUrls("google.pl");
        HttpRequest request = mock(HttpRequest.class);
        when(request.getUri()).thenReturn("http://sadih$@%@$@#");

        assertTrue(list.testHttpRequest(request, null));
    }

    private BlackListUrlsPlugin initializeBlackListUrls(String domainList){
        BlackListUrlsPlugin list = new BlackListUrlsPlugin();
        Map<String, Object> params = new HashMap<>();
        params.put("list",new SerializableList(Arrays.asList(domainList)));
        list.setParameters(params);
        list.init();

        return list;
    }

}
