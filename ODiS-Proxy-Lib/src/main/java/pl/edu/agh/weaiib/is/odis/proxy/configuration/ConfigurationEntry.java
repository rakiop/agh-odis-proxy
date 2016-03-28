package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.LinkedList;
import java.util.List;

/**
 * Configuration for one listener
 */
public class ConfigurationEntry {

    /**
     * Type of listener : HttpServer or SocketListener
     */
    @Attribute
    private ListenerType type;

    /**
     * Port to listen
     */
    @Attribute
    private int port;

    /**
     * List of filters/plugins - optional
     */
    @ElementList(name = "filters", entry = "filter", inline = true, required = false)
    private List<Filter> filters;

    /**
     * Default constructor to fill {@code filters} with {@link java.util.LinkedList}
     */
    public ConfigurationEntry(){
        this.filters = new LinkedList<>();
    }

    /**
     * Constructor to speed creation
     * @param port      Port to listen
     * @param type      Type of listener
     */
    public ConfigurationEntry(int port, ListenerType type){
        this();
        this.port = port;
        this.type = type;
    }

    /**
     * Add new filter/plugin to listener
     * @param filter    Filter
     * @throws IllegalArgumentException   If filter is null
     */
    public void addFilter(Filter filter){
        if(filter == null)
            throw new IllegalArgumentException("Filter cannot be null");
        filters.add(filter);
    }

    /**
     * Get listener type
     * @return
     */
    public ListenerType getType() {
        return type;
    }

    /**
     * Set listener type
     * @param type  Listener type
     */
    public void setType(ListenerType type) {
        this.type = type;
    }

    /**
     * Get list of filters/plugins
     * @return      List of filters/plugins
     */
    public List<Filter> getFilters() {
        return filters;
    }

    /**
     * Set list of filters/plugins
     * @param filters       List of filters / plugins
     * @throws IllegalArgumentException If list is null
     */
    public void setFilters(List<Filter> filters) {
        if(filters == null)
            throw new IllegalArgumentException("Filters list cannot be null");
        this.filters = filters;
    }

    /**
     * Get port to listen
     * @return
     */
    public int getPort() {
        return port;
    }

    /**
     * Set port to listen
     * @param port  Port to listen
     */
    public void setPort(int port) {
        this.port = port;
    }
}
