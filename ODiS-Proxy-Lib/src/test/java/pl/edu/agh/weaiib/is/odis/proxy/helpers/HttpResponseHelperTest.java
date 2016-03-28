package pl.edu.agh.weaiib.is.odis.proxy.helpers;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HttpResponseHelperTest {

    private static final String contentType = "text/html";
    private static final String contentCharset = "iso-8859-2";

    @Test
    public void returnEmptyContentTypeIfWrongType(){
        HttpObject object = mock(HttpObject.class);

        String contentType = HttpResponseHelper.getContentType(object);

        assertEquals(contentType, "");
    }

    @Test
    public void getContentType(){
        HttpResponse response = mock(HttpResponse.class);
        DefaultHttpHeaders headers = mock(DefaultHttpHeaders.class);

        when(response.headers()).thenReturn(headers);
        when(headers.entries()).thenReturn(getContentEntries());

        String type = HttpResponseHelper.getContentType(response);

        assertEquals(type, contentType);
    }

    @Test
    public void returnDefaultCharsetIfWrongType(){
        HttpObject object = mock(HttpObject.class);

        String contentType = HttpResponseHelper.getCharset(object);

        assertEquals(contentType, "UTF-8");
    }

    @Test
    public void getContentCharset(){
        HttpResponse response = mock(HttpResponse.class);
        DefaultHttpHeaders headers = mock(DefaultHttpHeaders.class);

        when(response.headers()).thenReturn(headers);
        when(headers.entries()).thenReturn(getContentEntries());

        String type = HttpResponseHelper.getCharset(response);

        assertEquals(contentCharset, type);
    }

    @Test
    public void updateContentSize(){
        HttpResponse response = mock(HttpResponse.class);
        DefaultHttpHeaders headers = mock(DefaultHttpHeaders.class);

        List<Map.Entry<String, String>> entries = getContentEntries();

        when(response.headers()).thenReturn(headers);
        when(headers.entries()).thenReturn(entries);

        int contentSizeId = 1;
        int contentLength = 100;

        HttpResponseHelper.updateContentLength(response, contentLength);

        assertEquals(entries.get(contentSizeId).getValue(), Integer.toString(contentLength));

    }


    private List<Map.Entry<String, String>> getContentEntries(){
        Map.Entry<String, String> typeEntry = new Map.Entry<String, String>() {
            private final String Value = String.format("%s; charset=%s", contentType, contentCharset);
            @Override
            public String getKey() {
                return "Content-Type";
            }

            @Override
            public String getValue() {
                return Value;
            }

            @Override
            public String setValue(String value) {
                return null;
            }
        };

        Map.Entry<String, String> sizeEntry = new Map.Entry<String, String>() {
            private String Value = "1";
            @Override
            public String getKey() {
                return "Content-Length";
            }

            @Override
            public String getValue() {
                return Value;
            }

            @Override
            public String setValue(String value) {
                Value = value;
                return Value;
            }
        };

        return Arrays.asList(typeEntry, sizeEntry);
    }

}
