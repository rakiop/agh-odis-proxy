package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import org.junit.Test;

import java.util.LinkedList;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ConfigurationEntryTest {

    private static final int port = 8080;
    private static final ListenerType listenerType = ListenerType.HTTP_SERVER;

    @Test
    public void useDefaultConstructorCreatedFilteresList(){
        ConfigurationEntry entry = new ConfigurationEntry();

        assertNotNull(entry.getFilters());
    }

    @Test
    public void constructorFillsParameters(){
        ConfigurationEntry entry = new ConfigurationEntry(port, listenerType);

        assertEquals(entry.getPort(), port);
        assertEquals(entry.getType(), listenerType);
        assertNotNull(entry.getFilters());
    }

    @Test
    public void settrsUpdatedParamters(){
        ConfigurationEntry entry = new ConfigurationEntry();

        LinkedList<Filter> filters = new LinkedList<>();

        entry.setPort(port);
        entry.setType(listenerType);
        entry.setFilters(filters);

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
        LinkedList<Filter> filters = new LinkedList<>();
        entry.setFilters(filters);

        assertEquals(entry.getFilters(), filters);
    }

    @Test(expected = IllegalArgumentException.class)
    public void setNullFiltersListThrowsException(){
        ConfigurationEntry entry = new ConfigurationEntry();
        entry.setFilters(null);
    }

}