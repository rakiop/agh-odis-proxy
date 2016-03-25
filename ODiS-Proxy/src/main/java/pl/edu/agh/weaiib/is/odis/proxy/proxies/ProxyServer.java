package pl.edu.agh.weaiib.is.odis.proxy.proxies;

import org.littleshoot.proxy.HttpProxyServer;
import org.littleshoot.proxy.impl.DefaultHttpProxyServer;
import org.littleshoot.proxy.mitm.RootCertificateException;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.helpers.DateHelper;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;
import pl.edu.agh.weaiib.is.odis.proxy.proxies.http.FilterAdapter;

import java.net.InetSocketAddress;
import java.time.LocalTime;
import java.util.*;

public class ProxyServer implements Proxies{

    private final int port;

    private HttpProxyServer server;

    private boolean serverIsStarted = false;

    private Map<FilterPlace, LinkedList<Filter>> filters = new HashMap<FilterPlace, LinkedList<Filter>>();

    private LocalTime fromTime;

    private LocalTime toTime;

    public ProxyServer(int port){
        this.port = port;
    }

    private static final LinkedList<Filter> emptyFilterList = new LinkedList<>();

    public List<Filter> getFilters(FilterPlace place){
        if(DateHelper.currentTimeIsBetween(fromTime, toTime)){
            List<Filter> list = this.filters.get(place);
            return list == null ? emptyFilterList : list;
        }
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

    public InetSocketAddress getAddress(){
        return server.getListenAddress();
    }

    @Override
    public void addFilter(Filter filter, FilterPlace place){
        if(!filters.containsKey(place))
            filters.put(place, new LinkedList<>());

        filters.get(place).add(filter);
        Collections.sort(filters.get(place),(f1,f2) -> f1.getPriority() - f2.getPriority());
    }

    @Override
    public void start() throws RootCertificateException {
        server = DefaultHttpProxyServer.bootstrap()
            .withAddress(new InetSocketAddress(port))
            .withFiltersSource(new FilterAdapter(this))
            .start();
        serverIsStarted = true;
    }

    @Override
    public void close() {
        server.stop();
        serverIsStarted = false;
    }

    @Override
    public boolean isStarted() {
        return serverIsStarted;
    }
}
