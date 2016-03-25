package pl.edu.agh.weaiib.is.odis.proxy.proxies;

import java.util.List;
import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;

public interface Proxies {

    void start() throws Exception;

    void close() throws Exception;

    boolean isStarted();

    void addFilter(Filter filter, FilterPlace place);

    List<Filter> getFilters(FilterPlace place);

}
