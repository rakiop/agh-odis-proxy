package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import pl.edu.agh.weaiib.is.odis.proxy.SerializableList;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Plugin that checks if content of response
 * contains forbidden terms or patterns
 */
public class ContentFilterPlugin extends ODiSHttpFilter {

    /**
     * List of forbidden patterns
     */
    private final List<Pattern> forbiddenPatterns = new LinkedList<>();

    /**
     * List of forbidden terms
     */
    private List<String> forbiddenTerms = new LinkedList<>();

    /**
     * Testing request is disabled - always return {@code true}
     * @param originalRequest   Request object
     * @param ctx               Context
     * @return                  Always return {@code true}
     */
    @Override
    public FilterResponse testHttpRequest(HttpRequest originalRequest, ChannelHandlerContext ctx) {
        return new FilterResponse(true);
    }

    /**
     * Check if response content contains forbidden terms or patterns
     * @param originalRequest   Request object
     * @param response          Response object
     * @param content           Parsed content of response
     * @return                  Whether content contains forbidden terms/pattern or not
     */
    @Override
    public FilterResponse testHttpResponse(HttpRequest originalRequest, HttpObject response, String content) {

        for(String term : forbiddenTerms){
            if(content.contains(term))
                return new FilterResponse(false, String.format("Contains forbidden term - %s", term));
        }

        for(Pattern pattern : forbiddenPatterns){
            Matcher matcher = pattern.matcher(content);
            if(matcher.find())
                return new FilterResponse(false, String.format("Contains forbidden term - %s", pattern.toString()));
        }

        return new FilterResponse(true);
    }

    /**
     * Read forbidden lists from {@code patterns} with keys "patterns" and "terms"
     * @throws  java.lang.IllegalArgumentException  If all lists doesn't exist or are empty
     */
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
