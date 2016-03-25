package pl.edu.agh.weaiib.is.odis.proxy.proxies.socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.ODiSSocketFIlter;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.SocketListener;

import java.io.IOException;
import java.net.Socket;
import java.util.List;

public class ODiSSocketClient implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ODiSSocketClient.class);

    private final SocketListener socketListener;

    private final Socket socket;

    public ODiSSocketClient(SocketListener socketListener, Socket socket){
        this.socketListener = socketListener;
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            List<Filter> filters = socketListener.getFilters(FilterPlace.SOCKET_BEFORE);

            for(Filter filter : filters){
                ((ODiSSocketFIlter)filter).testSocketRequest(socket);
            }
            LOGGER.info("Closing connection");
            socket.close();
        } catch (IOException e) {
            LOGGER.warn(String.format("Error while handling client: %s", e.getMessage()));
        }

    }
}
