package pl.edu.agh.weaiib.is.odis.proxy.proxies;

import java.util.List;
import org.junit.Test;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProxyServerTest {

    private static final int testPort = 8989;
    private static final FilterPlace place = FilterPlace.SOCKET_BEFORE;

    @Test
    public void addFilter(){
        ProxyServer server = new ProxyServer(testPort);

        Filter filter = mock(Filter.class);
        when(filter.getPriority()).thenReturn(1);

        server.addFilter(filter, place);

        assertTrue(server.getFilters(place).contains(filter));

    }

    @Test
    public void filtersAreOrdered(){
        Filter filter1 = mock(Filter.class);
        when(filter1.getPriority()).thenReturn(10);

        Filter filter2 = mock(Filter.class);
        when(filter2.getPriority()).thenReturn(1);

        ProxyServer server = new ProxyServer(testPort);
        server.addFilter(filter1, place);
        server.addFilter(filter2, place);

        assertEquals(server.getFilters(place).get(0), filter2);
        assertEquals(server.getFilters(place).get(1), filter1);
    }

    @Test
    public void getFilterAlwaysReturnsList(){
        ProxyServer server = new ProxyServer(testPort);
        List<Filter> filters = server.getFilters(place);

        assertNotNull(filters);
    }

}