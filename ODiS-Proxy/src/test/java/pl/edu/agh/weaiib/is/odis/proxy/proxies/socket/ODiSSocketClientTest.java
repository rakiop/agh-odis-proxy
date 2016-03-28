package pl.edu.agh.weaiib.is.odis.proxy.proxies.socket;

import org.junit.Test;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.ODiSSocketFilter;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.SocketListener;

import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.LinkedList;

import static org.mockito.Mockito.*;

public class ODiSSocketClientTest {

    @Test
    public void getOnlySocketFilters(){
        SocketListener listener = mock(SocketListener.class);
        Socket socket = mock(Socket.class);

        when(listener.getFilters(FilterPlace.SOCKET_BEFORE)).thenReturn(new LinkedList<>());

        ODiSSocketClient client = new ODiSSocketClient(listener, socket);
        client.run();

        verify(listener, times(1)).getFilters(any());
        verify(listener, times(1)).getFilters(FilterPlace.SOCKET_BEFORE);
    }

    @Test
    public void runTestSocketRequest(){
        SocketListener listener = mock(SocketListener.class);
        ODiSSocketFilter filter = mock(ODiSSocketFilter.class);
        Socket socket = mock(Socket.class);

        when(listener.getFilters(FilterPlace.SOCKET_BEFORE)).thenReturn(Arrays.asList(filter));

        ODiSSocketClient client = new ODiSSocketClient(listener, socket);
        client.run();

        verify(filter, times(1)).testSocketRequest(socket);
    }

    /**
     * Dummy test to line coverage...
     * @throws IOException
     */
    @Test
    public void closingSocketThrowsException() throws IOException {
        SocketListener listener = mock(SocketListener.class);
        ODiSSocketFilter filter = mock(ODiSSocketFilter.class);
        Socket socket = mock(Socket.class);

        when(listener.getFilters(FilterPlace.SOCKET_BEFORE)).thenReturn(Arrays.asList(filter));
        doThrow(new IOException()).when(socket).close();

        ODiSSocketClient client = new ODiSSocketClient(listener, socket);
        client.run();
    }
}
