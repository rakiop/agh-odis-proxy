package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="configuration")
public class ConfigurationEntry {

    private String timeFrom;

    private String timeTo;

    private ListenerType type;

    private int port;

    private List<Filter> filters;

    public ConfigurationEntry(){
        this.filters = new LinkedList<Filter>();
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

    @XmlElementWrapper(name="filters")
    @XmlElement(name="filter")
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
