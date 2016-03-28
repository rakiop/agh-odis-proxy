package pl.edu.agh.weaiib.is.odis.proxy.proxies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.FilterPredicate;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.socket.ODiSSocketClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Socket listener implementation
 */
public class SocketListener implements Proxies{

    /**
     * Main application logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(SocketListener.class);

    /**
     * Port to listen
     */
    private final int port;

    /**
     * Real socket listener
     */
    private ServerSocket socket;

    /**
     * List of filters
     */
    private final List<Filter> filters;

    /**
     * Predicate to filter plugins by time
     */
    private static final FilterPredicate filterPredicate = new FilterPredicate();

    /**
     * Thread to listen to not block main thread
     */
    private SocketListenerThread listenerThread;

    /**
     * Constructor with port
     * @param port  Port to listen
     */
    public SocketListener(int port){
        this.port = port;
        filters = new LinkedList<>();
    }

    /**
     * Start listening
     * @throws IOException      If port is already blocked
     */
    @Override
    public void start() throws IOException {
        socket = new ServerSocket(port);

        listenerThread = new SocketListenerThread(this, socket);

        new Thread(listenerThread).start();
    }

    /**
     * Stop listening
     * @throws Exception    If something unexpected happen
     */
    @Override
    public void close() throws Exception {
        if(listenerThread != null)
            listenerThread.interrupt();

        if(socket != null && !socket.isClosed())
            socket.close();
    }

    /**
     * Is socket listening?
     * @return  Is socket listening?
     */
    @Override
    public boolean isStarted() {
        return socket != null && !socket.isClosed() && socket.isBound();
    }

    /**
     * Add filter / plugin
     * @param filter    Filter / plugin
     * @param place     Place to inject
     */
    @Override
    public void addFilter(Filter filter, FilterPlace place) {
        filters.add(filter);
        Collections.sort(filters,(f1,f2) -> f1.getPriority() - f2.getPriority());
    }

    /**
     * Get list of plugin filtered by time ( place is ignored)
     * @param place     Place of injection
     * @return          List of plugins
     */
    @Override
    public List<Filter> getFilters(FilterPlace place) {
        return filters.stream()
                .filter(filterPredicate)
                .collect(Collectors.toList());
    }

    /**
     * Class to handle listening without blocking main thread
     */
    private static class SocketListenerThread implements Runnable{

        /**
         * Socket handler
         */
        private final ServerSocket socket;

        /**
         * Listener object to get filters
         */
        private final SocketListener listener;

        /**
         * To peacefully close connection
         */
        private boolean interrupt;

        /**
         * Default construcotr
         * @param listener  Listener object
         * @param socket    Real socket object
         */
        public SocketListenerThread(SocketListener listener, ServerSocket socket){
            this.listener = listener;
            this.socket = socket;
            this.interrupt = false;
        }

        /**
         * Listen on socket and start new thread to handle clients
         */
        @Override
        public void run() {
            LOGGER.info(String.format("Start listening on socket %s:%s", socket.getInetAddress(), socket.getLocalPort()));
            while(!interrupt && listener.isStarted()){
                try {
                    Socket client = socket.accept();
                    LOGGER.info(String.format("New socket request from %s", client.getRemoteSocketAddress()));
                    ODiSSocketClient socketClient = new ODiSSocketClient(listener, client);
                    new Thread(socketClient).start();
                } catch (Exception e) {
                    LOGGER.warn(String.format("Socket client thread error: %s", e.getMessage()));
                }
            }
        }

        /**
         * Call to break loop
         */
        public void interrupt(){
            this.interrupt = true;
        }

    }

}
