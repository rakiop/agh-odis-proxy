package pl.edu.agh.weaiib.is.odis.proxy.proxies;

import org.junit.Test;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;

import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SocketListenerTest {

    private static final int testPort = 8989;
    private static final FilterPlace place = FilterPlace.SOCKET_BEFORE;

    @Test
    public void addFilter(){
        SocketListener listener = new SocketListener(testPort);

        Filter filter = mock(Filter.class);
        when(filter.getPriority()).thenReturn(1);

        listener.addFilter(filter, place);

        assertTrue(listener.getFilters(place).contains(filter));
    }

    @Test
    public void filtersAreOrdered(){
        Filter filter1 = mock(Filter.class);
        when(filter1.getPriority()).thenReturn(10);

        Filter filter2 = mock(Filter.class);
        when(filter2.getPriority()).thenReturn(1);

        SocketListener listener = new SocketListener(testPort);
        listener.addFilter(filter1, place);
        listener.addFilter(filter2, place);

        assertEquals(listener.getFilters(place).get(0), filter2);
        assertEquals(listener.getFilters(place).get(1), filter1);
    }

    @Test
    public void getFilterAlwaysReturnsList(){
        SocketListener listener = new SocketListener(testPort);
        List<Filter> filters = listener.getFilters(place);

        assertNotNull(filters);
    }

    /*
    @Test
    public void getFilterListOnlyIfTimeIsCorrect(){
        FilterPlace place = FilterPlace.SERVER_CLIENT_TO_PROXY;

        SocketListener listener = new SocketListener(testPort);

        Filter filter = mock(Filter.class);
        listener.addFilter(filter, place);

        // Current is between
        LocalTime before = LocalTime.now().minusSeconds(5);
        LocalTime after = LocalTime.now().plusSeconds(5);
        listener.setFilterFromTime(before);
        listener.setFilterToTime(after);

        List<Filter> filters = listener.getFilters(place);
        assertFalse(filters.isEmpty());

        // Current is not between
        before = LocalTime.now().plusSeconds(5);
        after = before.plusSeconds(5);
        listener.setFilterFromTime(before);
        listener.setFilterToTime(after);

        filters = listener.getFilters(place);
        assertTrue(filters.isEmpty());
    }
    */
}