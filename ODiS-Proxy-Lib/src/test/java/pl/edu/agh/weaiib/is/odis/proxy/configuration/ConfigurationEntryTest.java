package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import java.util.LinkedList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

/**
 * Created by SG0222582 on 3/24/2016.
 */
public class ConfigurationEntryTest {

    private static final String fromTime = "12PM";
    private static final String toTime = "12AM";
    private static final int port = 8080;
    private static final ListenerType listenerType = ListenerType.HTTP_SERVER;

    @Test
    public void useDefaultConstructorCreatedFilteresList(){
        ConfigurationEntry entry = new ConfigurationEntry();

        assertNotNull(entry.getFilters());
    }

    @Test
    public void constructorFillsParameters(){
        ConfigurationEntry entry = new ConfigurationEntry(fromTime, toTime, port, listenerType);

        assertEquals(entry.getTimeFrom(), fromTime);
        assertEquals(entry.getTimeTo(), toTime);
        assertEquals(entry.getPort(), port);
        assertEquals(entry.getType(), listenerType);
        assertNotNull(entry.getFilters());
    }

    @Test
    public void settrsUpdatedParamters(){
        ConfigurationEntry entry = new ConfigurationEntry();

        LinkedList<Filter> filters = new LinkedList<Filter>();

        entry.setTimeFrom(fromTime);
        entry.setTimeTo(toTime);
        entry.setPort(port);
        entry.setType(listenerType);
        entry.setFilters(filters);

        assertEquals(entry.getTimeFrom(), fromTime);
        assertEquals(entry.getTimeTo(), toTime);
        assertEquals(entry.getPort(), port);
        assertEquals(entry.getType(), listenerType);
        assertEquals(entry.getFilters(), filters);
    }

    @Test
    public void addNewFilter(){
        ConfigurationEntry entry = new ConfigurationEntry();
        Filter filter = mock(Filter.class);

        entry.addFilter(filter);

        assertTrue(entry.getFilters().contains(filter));
    }

    @Test(expected = IllegalArgumentException.class)
    public void addNullFilterThrowsException(){
        ConfigurationEntry entry = new ConfigurationEntry();
        entry.addFilter(null);
    }

    @Test
    public void setNewFiltersList(){
        ConfigurationEntry entry = new ConfigurationEntry();
        LinkedList<Filter> filters = new LinkedList<Filter>();
        entry.setFilters(filters);

        assertEquals(entry.getFilters(), filters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullFiltersListThrowsException(){
        ConfigurationEntry entry = new ConfigurationEntry();
        entry.setFilters(null);
    }

}