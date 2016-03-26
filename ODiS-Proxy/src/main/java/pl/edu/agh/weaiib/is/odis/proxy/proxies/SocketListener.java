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

public class SocketListener implements Proxies{

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketListener.class);

    private final int port;

    private ServerSocket socket;

    private final List<Filter> filters;

    private SocketListenerThread listenerThread;

    public SocketListener(int port){
        this.port = port;
        filters = new LinkedList<>();
    }

    @Override
    public void start() throws IOException {
        socket = new ServerSocket(port);

        listenerThread = new SocketListenerThread(this, socket);

        new Thread(listenerThread).start();
    }

    @Override
    public void close() throws Exception {
        if(listenerThread != null)
            listenerThread.interrupt();

        if(socket != null && !socket.isClosed())
            socket.close();
    }

    @Override
    public boolean isStarted() {
        return socket != null && !socket.isClosed() && socket.isBound();
    }

    @Override
    public void addFilter(Filter filter, FilterPlace place) {
        filters.add(filter);
        Collections.sort(filters,(f1,f2) -> f1.getPriority() - f2.getPriority());
    }

    private static final FilterPredicate filterPredicate = new FilterPredicate();

    @Override
    public List<Filter> getFilters(FilterPlace place) {
        return filters.stream()
                .filter(filterPredicate)
                .collect(Collectors.toList());
    }

    private static class SocketListenerThread implements Runnable{

        private final ServerSocket socket;
        private final SocketListener listener;

        private boolean interrupt;

        public SocketListenerThread(SocketListener listener, ServerSocket socket){
            this.listener = listener;
            this.socket = socket;
            this.interrupt = false;
        }

        @Override
        public void run() {
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

        public void interrupt(){
            this.interrupt = true;
        }

    }

}
