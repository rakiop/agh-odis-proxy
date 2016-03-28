package pl.edu.agh.weaiib.is.odis.proxy.proxies;

import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.mitm.RootCertificateException;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.FilterPredicate;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.http.FilterAdapter;

import java.net.InetSocketAddress;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Http proxy server
 */
public class ProxyServer implements Proxies{

    /**
     * Port to listen
     */
    private final int port;

    /**
     * Real Proxy server
     */
    private HttpProxyServer server;

    /**
     * Is proxy started
     */
    private boolean serverIsStarted = false;

    /**
     * Map of filters
     */
    private final Map<FilterPlace, LinkedList<Filter>> filters = new HashMap<>();

    /**
     * Empty filter / plugins list if not set - performance
     */
    private static final LinkedList<Filter> emptyFilterList = new LinkedList<>();

    /**
     * Predicate to filter by time
     */
    private static final FilterPredicate filterPredicate = new FilterPredicate();

    /**
     * Constructor
     * @param port  Port to listen
     */
    public ProxyServer(int port){
        this.port = port;
    }

    /**
     * Get filter for specific place filtered by current time
     * @param place     Place of injection
     * @return          Filtered list of plugins
     */
    public List<Filter> getFilters(FilterPlace place){
        List<Filter> list = this.filters.get(place);
        if(list == null)
            return emptyFilterList;

        return list.stream()
                    .filter(filterPredicate)
                    .collect(Collectors.toList());
    }

    /**
     * Listening address
     * @return  Listening address
     */
    public InetSocketAddress getAddress(){
        return server.getListenAddress();
    }

    /**
     * Adde new Filter
     * @param filter    Filter / plugin
     * @param place     Place to inject
     */
    @Override
    public void addFilter(Filter filter, FilterPlace place){
        if(!filters.containsKey(place))
            filters.put(place, new LinkedList<>());

        filters.get(place).add(filter);
        Collections.sort(filters.get(place),(f1,f2) -> f1.getPriority() - f2.getPriority());
    }

    /**
     * Start http server
     * @throws RootCertificateException If SSL connection start fails
     */
    @Override
    public void start() throws RootCertificateException {
        server = DefaultHttpProxyServer.bootstrap()
            .withAddress(new InetSocketAddress(port))
            .withFiltersSource(new FilterAdapter(this))
            .start();
        serverIsStarted = true;
    }

    /**
     * Stop listening
     */
    @Override
    public void close() {
        if(server != null)
            server.stop();
        serverIsStarted = false;
    }

    /**
     * Is proxy listening?
     * @return  Is proxy listening?
     */
    @Override
    public boolean isStarted() {
        return serverIsStarted;
    }
}
