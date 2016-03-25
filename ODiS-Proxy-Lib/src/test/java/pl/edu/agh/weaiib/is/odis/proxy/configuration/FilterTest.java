package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FilterTest {

    private static final String filterName = "filterName";
    private static final FilterPlace place = FilterPlace.SERVER_CLIENT_TO_PROXY;
    private static final int priority = 1;

    @Test
    public void emptyConstructorCreatesParametersMap(){
        Filter filter = new Filter();
        assertNotNull(filter.getParameters());
    }

    @Test
    public void constructorFillsProperties(){
        Filter filter = new Filter(filterName, place, priority);

        assertEquals(filter.getFilterName(), filterName);
        assertEquals(filter.getPlace(), place);
        assertEquals(filter.getPriority(), priority);

        assertNotNull(filter.getParameters());
    }

    @Test
    public void settersFillsParameters(){
        Filter filter = new Filter();

        HashMap<String, String> parameters = new HashMap<String, String>();

        filter.setFilterName(filterName);
        filter.setPlace(place);
        filter.setPriority(priority);
        filter.setParameters(parameters);

        assertEquals(filter.getFilterName(), filterName);
        assertEquals(filter.getPlace(), place);
        assertEquals(filter.getPriority(), priority);
        assertEquals(filter.getParameters(), parameters);
    }

    @Test
    public void nullParametersMapIsNotSet(){
        Filter filter = new Filter();
        filter.setParameters(null);

        assertNotNull(filter.getParameters());
    }

    @Test
    public void addParameter(){
        String key = "key";
        String value = "value";

        Filter filter = new Filter();
        filter.addParameter(key, value);

        assertTrue(filter.getParameters().containsKey(key));
        assertEquals(filter.getParameters().get(key), value);

    }

}