package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;

import java.util.LinkedList;
import java.util.List;

public class ConfigurationEntry {

    @Attribute
    private String timeFrom;

    @Attribute
    private String timeTo;

    @Attribute
    private ListenerType type;

    @Attribute
    private int port;

    @ElementList(name = "filters", entry = "filter", inline = true, required = false)
    private List<Filter> filters;

    public ConfigurationEntry(){
        this.filters = new LinkedList<>();
    }

    public ConfigurationEntry(String timeFrom, String timeTo, int port, ListenerType type){
        this();
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.port = port;
        this.type = type;
    }

    public void addFilter(Filter filter){
        if(filter == null)
            throw new IllegalArgumentException("Filter cannot be null");
        filters.add(filter);
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public ListenerType getType() {
        return type;
    }

    public void setType(ListenerType type) {
        this.type = type;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        if(filters == null)
            throw new IllegalArgumentException("Filters list cannot be null");
        this.filters = filters;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
