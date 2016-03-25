package pl.edu.agh.weaiib.is.odis.proxy.proxies;

import org.junit.Test;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
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

}