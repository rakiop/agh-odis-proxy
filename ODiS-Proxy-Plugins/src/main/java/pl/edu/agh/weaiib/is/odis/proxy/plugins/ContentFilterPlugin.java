package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import pl.edu.agh.weaiib.is.odis.proxy.SerializableList;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContentFilterPlugin extends ODiSHttpFilter {

    List<Pattern> forbiddenPatterns = new LinkedList<>();

    List<String> forbiddenTerms = new LinkedList();

    @Override
    public boolean testHttpRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        return true;
    }

    @Override
    public boolean testHttpResponse(HttpRequest originalRequest, HttpObject response, String content) {

        for(String term : forbiddenTerms){
            if(content.contains(term))
                return false;
        }

        for(Pattern pattern : forbiddenPatterns){
            Matcher matcher = pattern.matcher(content);
            if(matcher.find())
                return false;
        }

        return true;
    }

    @Override
    public void init() {
        SerializableList patterns = (SerializableList) parameters.get("patterns");
        SerializableList terms = (SerializableList) parameters.get("terms");

        if((patterns == null || patterns.getList() == null || patterns.getList().isEmpty())&&
                (terms == null || terms.getList() == null || terms.getList().isEmpty()))
            throw new IllegalArgumentException("Need at least one forbidden list");

        if(terms != null && terms.getList() != null)
            forbiddenTerms = terms.getList();

        if(patterns != null && patterns.getList() != null){
            for(String pattern : patterns.getList()){
                forbiddenPatterns.add(Pattern.compile(pattern));
            }
        }
    }
}
