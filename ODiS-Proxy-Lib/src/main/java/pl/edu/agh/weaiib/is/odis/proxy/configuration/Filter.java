package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementMap;

import java.util.HashMap;
import java.util.Map;

public class Filter {

    @Attribute
    private String filterName;

    @Attribute
    private FilterPlace place;

    @Attribute
    private int priority;

    @Attribute(required = false)
    private String timeFrom;

    @Attribute(required = false)
    private String timeTo;

    @ElementMap(inline = true, entry = "parameter", key = "key", value = "value", required = false)
    private Map<String, Object> parameters;

    public Filter(){
        parameters = new HashMap<>();
    };

    public Filter(String filterName, FilterPlace place, int priority){
        this(filterName, place, priority, null, null);
    }

    public Filter(String filterName, FilterPlace place, int priority, String timeFrom, String timeTo){
        this();
        this.filterName = filterName;
        this.place = place;
        this.priority = priority;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    public void addParameter(String key, Object value){
        parameters.put(key, value);
    }

    public String getFilterName() {
        return filterName;
    }

    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }


    public FilterPlace getPlace() {
        return place;
    }

    public void setPlace(FilterPlace place) {
        this.place = place;
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

    public Map<String, Object> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Object> parameters) {
        if(parameters != null) {
            this.parameters = parameters;
        }
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
