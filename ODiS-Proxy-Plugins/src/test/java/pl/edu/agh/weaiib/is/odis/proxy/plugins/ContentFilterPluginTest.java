package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import org.junit.Test;
import pl.edu.agh.weaiib.is.odis.proxy.SerializableList;

import java.util.*;
import java.util.regex.PatternSyntaxException;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertFalse;

public class ContentFilterPluginTest {

    private static final String patternsKey = "patterns";
    private static final String termsKey = "terms";

    @Test(expected = IllegalArgumentException.class)
    public void initChecksIfAnyListIsInParameters(){
        Map<String, Object> properties = new HashMap<>();

        ContentFilterPlugin plugin = new ContentFilterPlugin();
        plugin.setParameters(properties);
    }

    @Test(expected = IllegalArgumentException.class)
    public void initChecksIfAnyListIsInSerializableList(){
        Map<String, Object> properties = new HashMap<>();

        properties.put(patternsKey, new SerializableList());
        properties.put(termsKey, new SerializableList());

        ContentFilterPlugin plugin = new ContentFilterPlugin();
        plugin.setParameters(properties);
    }

    @Test(expected = IllegalArgumentException.class)
    public void initChecksIfAnyListIsAintEmpty(){
        Map<String, Object> properties = new HashMap<>();

        properties.put(patternsKey, new SerializableList(new LinkedList<>()));
        properties.put(termsKey, new SerializableList(new LinkedList<>()));

        ContentFilterPlugin plugin = new ContentFilterPlugin();
        plugin.setParameters(properties);
    }

    @Test(expected = PatternSyntaxException.class)
    public void checkPatternSyntax(){
        Map<String, Object> properties = new HashMap<>();

        List<String> patterns = Arrays.asList("([)][].*");

        properties.put(patternsKey, new SerializableList(patterns));

        ContentFilterPlugin plugin = new ContentFilterPlugin();
        plugin.setParameters(properties);
    }

    @Test
    public void testHttpRequestAlwaysTrue(){
        ContentFilterPlugin plugin = new ContentFilterPlugin();

        boolean test = plugin.testHttpRequest(null, null);

        assertTrue(test);
    }

    @Test
    public void breakIfForbiddenTermIsFound(){
        String content = "Go go power rangers";

        List<String> terms = Arrays.asList("go");

        Map<String, Object> properties = new HashMap<>();
        properties.put(termsKey, new SerializableList(terms));

        ContentFilterPlugin plugin = new ContentFilterPlugin();
        plugin.setParameters(properties);

        boolean test = plugin.testHttpResponse(null, null, content);

        assertFalse(test);
    }

    @Test
    public void breakIfForbiddenPatternIsFound(){
        String content = "Go go power rangers!!!";

        List<String> patterns = Arrays.asList("rangers([!]{2,})");

        Map<String, Object> properties = new HashMap<>();
        properties.put(patternsKey, new SerializableList(patterns));

        ContentFilterPlugin plugin = new ContentFilterPlugin();
        plugin.setParameters(properties);

        boolean test = plugin.testHttpResponse(null, null, content);

        assertFalse(test);
    }

    @Test
    public void passIfNeitherPatternOrTermFound(){
        String content = "Hue hue hue that was so funny :)";

        List<String> patterns = Arrays.asList("rangers([!]{2,})");
        List<String> terms = Arrays.asList("go");

        Map<String, Object> properties = new HashMap<>();
        properties.put(patternsKey, new SerializableList(patterns));
        properties.put(termsKey, new SerializableList(terms));

        ContentFilterPlugin plugin = new ContentFilterPlugin();
        plugin.setParameters(properties);

        boolean test = plugin.testHttpResponse(null, null, content);

        assertTrue(test);
    }



}
