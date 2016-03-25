package pl.edu.agh.weaiib.is.odis.proxy.proxies;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;

public class SocketListener implements Proxies{

    private final int port;

    private ServerSocket socket;

    private final List<Filter> filters;

    public SocketListener(int port){
        this.port = port;
        filters = new LinkedList<>();
    }

    @Override
    public void start() throws IOException {
        socket = new ServerSocket(port);

        //Socket client = socket.accept();
        //client.getRemoteSocketAddress().toString(); //sender address
    }

    @Override
    public void close() throws Exception {
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

    @Override
    public List<Filter> getFilters(FilterPlace place) {
        return filters;
    }
}
