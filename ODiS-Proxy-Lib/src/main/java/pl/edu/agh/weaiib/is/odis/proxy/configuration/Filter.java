package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementMap;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration of Filter / Plugin
 */
public class Filter {

    /**
     * Filter / Plugin class
     */
    @Attribute
    private String filterName;

    /**
     * Place to inject
     */
    @Attribute
    private FilterPlace place;

    /**
     * Priority (UNIX typed)
     */
    @Attribute
    private int priority;

    /**
     * Time start of filtering (working in pair with {@code timeTo}
     */
    @Attribute(required = false)
    private String timeFrom;

    /**
     * Time end of filtering (working in pair with {@code timeFrom}
     */
    @Attribute(required = false)
    private String timeTo;

    /**
     * Map of parameters - optional
     */
    @ElementMap(inline = true, entry = "parameter", key = "key", value = "value", required = false)
    private Map<String, Object> parameters;

    /**
     * Default constructor to fill {@code parameters} with {@link java.util.HashMap}
     */
    public Filter(){
        parameters = new HashMap<>();
    };

    /**
     * Speed constructor - not times
     * @param filterName    Filter class
     * @param place         Place to inject
     * @param priority      Priority
     */
    public Filter(String filterName, FilterPlace place, int priority){
        this(filterName, place, priority, null, null);
    }

    /**
     * Speed constructor
     * @param filterName    Filter class
     * @param place         Place to inject
     * @param priority      Priority
     * @param timeFrom      Start time of filtering
     * @param timeTo        End time of filtering
     */
    public Filter(String filterName, FilterPlace place, int priority, String timeFrom, String timeTo){
        this();
        this.filterName = filterName;
        this.place = place;
        this.priority = priority;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    /**
     * Add new parameter
     * @param key       Name of parameter
     * @param value     Value of parameter
     */
    public void addParameter(String key, Object value){
        parameters.put(key, value);
    }

    /**
     * Get filter class
     * @return  Filter class
     */
    public String getFilterName() {
        return filterName;
    }

    /**
     * Set filter class
     * @param filterName    New filter class name
     */
    public void setFilterName(String filterName) {
        this.filterName = filterName;
    }

    /**
     * Get place of injection
     * @return  Place of inject
     */
    public FilterPlace getPlace() {
        return place;
    }

    /**
     * Set place of inect
     * @param place Place to inject
     */
    public void setPlace(FilterPlace place) {
        this.place = place;
    }

    /**
     * Get start time of filtering
     * @return  Start time of filtering
     */
    public String getTimeFrom() {
        return timeFrom;
    }

    /**
     * Set start time of filtering
     * @param timeFrom  New start time
     */
    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    /**
     * Get end time of filtering
     * @return  End time of filtering
     */
    public String getTimeTo() {
        return timeTo;
    }

    /**
     * Set new end time of filtering
     * @param timeTo    New end time
     */
    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    /**
     * Get filter / plugin parameters
     * @return  Parameter
     */
    public Map<String, Object> getParameters() {
        return parameters;
    }

    /**
     * Set parameters if not null
     * @param parameters    New parameters
     */
    public void setParameters(Map<String, Object> parameters) {
        if(parameters != null) {
            this.parameters = parameters;
        }
    }

    /**
     * Get priority
     * @return  Priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Set priority
     * @param priority  New priority
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
