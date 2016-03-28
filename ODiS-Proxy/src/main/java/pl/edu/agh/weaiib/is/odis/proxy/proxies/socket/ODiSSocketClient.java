package pl.edu.agh.weaiib.is.odis.proxy.proxies.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.ODiSSocketFilter;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.SocketListener;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 * Handle one client from socket listener
 */
public class ODiSSocketClient implements Runnable {

    /**
     * Main application logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ODiSSocketClient.class);

    /**
     * Socket listener
     */
    private final SocketListener socketListener;

    /**
     * Client socket
     */
    private final Socket socket;

    /**
     * Default constructor
     * @param socketListener    socket listener
     * @param socket            client socket
     */
    public ODiSSocketClient(SocketListener socketListener, Socket socket){
        this.socketListener = socketListener;
        this.socket = socket;
    }

    /**
     * Calls plugins on client socket and closes connection
     */
    @Override
    public void run() {
        try {
            List<Filter> filters = socketListener.getFilters(FilterPlace.SOCKET_BEFORE);

            for(Filter filter : filters){
                ((ODiSSocketFilter)filter).testSocketRequest(socket);
            }
            LOGGER.info("Closing connection");
            socket.close();
        } catch (IOException e) {
            LOGGER.warn(String.format("Error while handling client: %s", e.getMessage()));
        }

    }
}
