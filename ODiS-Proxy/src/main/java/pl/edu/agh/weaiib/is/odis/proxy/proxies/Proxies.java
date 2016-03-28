package pl.edu.agh.weaiib.is.odis.proxy.proxies;

import pl.edu.agh.weaiib.is.odis.proxy.configuration.FilterPlace;
import pl.edu.agh.weaiib.is.odis.proxy.plugins.Filter;

import java.util.List;

/**
 * Main interface of proxy / listener
 */
public interface Proxies {

    /**
     * Start listening
     * @throws Exception
     */
    void start() throws Exception;

    /**
     * End listening
     * @throws Exception
     */
    void close() throws Exception;

    /**
     * Is listening?
     * @return  Is listener started?
     */
    boolean isStarted();

    /**
     * Add new filter / plugin
     * @param filter    Filter / plugin
     * @param place     Place to inject
     */
    void addFilter(Filter filter, FilterPlace place);

    /**
     * Get all filters for specific place
     * @param place     Place of injection
     * @return          List of filters / plugins
     */
    List<Filter> getFilters(FilterPlace place);

}
