package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class FilterTest {

    private static final String filterName = "filterName";
    private static final FilterPlace place = FilterPlace.SERVER_CLIENT_TO_PROXY;
    private static final int priority = 1;
    private static final String timeFrom = "00:00";
    private static final String timeTo = "23:59:59";

    @Test
    public void emptyConstructorCreatesParametersMap(){
        Filter filter = new Filter();
        assertNotNull(filter.getParameters());
    }

    @Test
    public void constructorFillsProperties(){
        Filter filter = new Filter(filterName, place, priority, timeFrom, timeTo);

        assertEquals(filter.getFilterName(), filterName);
        assertEquals(filter.getPlace(), place);
        assertEquals(filter.getPriority(), priority);
        assertEquals(filter.getTimeFrom(), timeFrom);
        assertEquals(filter.getTimeTo(), timeTo);

        assertNotNull(filter.getParameters());
    }

    @Test
    public void settersFillsParameters(){
        Filter filter = new Filter();

        HashMap<String, Object> parameters = new HashMap<>();

        filter.setFilterName(filterName);
        filter.setPlace(place);
        filter.setPriority(priority);
        filter.setTimeFrom(timeFrom);
        filter.setTimeTo(timeTo);
        filter.setParameters(parameters);

        assertEquals(filter.getFilterName(), filterName);
        assertEquals(filter.getPlace(), place);
        assertEquals(filter.getPriority(), priority);
        assertEquals(filter.getTimeFrom(), timeFrom);
        assertEquals(filter.getTimeTo(), timeTo);
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