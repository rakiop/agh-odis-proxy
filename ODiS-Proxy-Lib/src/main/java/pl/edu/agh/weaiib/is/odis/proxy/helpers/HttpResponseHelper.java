package pl.edu.agh.weaiib.is.odis.proxy.helpers;

import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpResponse;
import java.util.AbstractMap;
import java.util.Map;

public class HttpResponseHelper {

    private static final String CONTENT_TYPE_KEY = "Content-Type";
    private static final String CONTENT_SIZE = "Content-Length";

    public static String getContentType(HttpObject httpObject){
        if(httpObject instanceof HttpResponse){
            DefaultHttpHeaders headers = (DefaultHttpHeaders)((HttpResponse) httpObject).headers();
            for (Map.Entry<String, String> entry: headers.entries()) {
                if(entry.getKey().equals(CONTENT_TYPE_KEY)) {
                    String[] values = entry.getValue().split(";");
                    return values[0].trim();
                }
            }
        }
        return "";
    }

    public static String getCharset(HttpObject httpObject){
        if(httpObject instanceof HttpResponse){
            DefaultHttpHeaders headers = (DefaultHttpHeaders)((HttpResponse) httpObject).headers();
            for (Map.Entry<String, String> entry: headers.entries()) {
                if(entry.getKey().equals(CONTENT_TYPE_KEY)) {
                    String[] values = entry.getValue().split(";");
                    String charsetWithKey = values.length > 1 ? values[1] : "";
                    String[] charset = charsetWithKey.split("=");
                    return charset.length > 1 ? charset[1].trim() : "UTF-8";
                }
            }
        }
        return "UTF-8";
    }

    public static void updateContentLength(HttpObject httpObject, int length) {
        DefaultHttpHeaders headers = (DefaultHttpHeaders)((HttpResponse) httpObject).headers();
        int index = -1;
        for(int i =0; i< headers.entries().size(); i++){
            if(headers.entries().get(i).getKey().equals(CONTENT_SIZE) ){
                index = i;
                break;
            }
        }

        if(index>=0){
            headers.entries().set(index, new AbstractMap.SimpleEntry<String, String>(CONTENT_SIZE, Integer.toString(length)));
        }
    }
}
