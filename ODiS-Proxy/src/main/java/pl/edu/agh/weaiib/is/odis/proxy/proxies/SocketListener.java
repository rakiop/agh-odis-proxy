package pl.edu.agh.weaiib.is.odis.proxy.proxies;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.helpers.DateHelper;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.socket.ODiSSocketClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalTime;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class SocketListener implements Proxies{

    private static final Logger LOGGER = LoggerFactory.getLogger(SocketListener.class);

    private final int port;

    private ServerSocket socket;

    private final List<Filter> filters;

    private LocalTime fromTime;

    private LocalTime toTime;

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

    private static final LinkedList<Filter> emptyFilterList = new LinkedList<>();

    @Override
    public List<Filter> getFilters(FilterPlace place) {
        if(DateHelper.currentTimeIsBetween(fromTime, toTime))
            return filters;
        else
            return emptyFilterList;
    }

    @Override
    public void setFilterFromTime(LocalTime date) {
        this.fromTime = date;
    }

    @Override
    public LocalTime getFilterFromTime() {
        return fromTime;
    }

    @Override
    public void setFilterToTime(LocalTime date) {
        this.toTime = date;
    }

    @Override
    public LocalTime getFilterToTime() {
        return toTime;
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
