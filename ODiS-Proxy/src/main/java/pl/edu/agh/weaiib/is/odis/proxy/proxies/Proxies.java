package pl.edu.agh.weaiib.is.odis.proxy.proxies;

import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;

import java.util.List;

public interface Proxies {

    void start() throws Exception;

    void close() throws Exception;

    boolean isStarted();

    void addFilter(Filter filter, FilterPlace place);

    List<Filter> getFilters(FilterPlace place);

}
