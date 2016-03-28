package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import java.time.LocalTime;
import java.util.Map;

/**
 * Filter / Plugin interface with basic implementation
 */
public abstract class Filter {

    /**
     * Plugin priority in UNIX type
     */
    private int priority;

    /**
     * Start filtering time
     */
    private LocalTime filterFrom;

    /**
     * End filtering time
     */
    private LocalTime filterTo;

    /**
     * Parameters list
     */
    protected Map<String, Object> parameters;

    /**
     * Set priority of plugin
     * @param priority  Priority
     */
    public void setPriority(int priority){
        this.priority = priority;
    }

    /**
     * Get plugin priority
     * @return  Priority
     */
    public int getPriority(){
        return priority;
    }

    /**
     * Set plugin parameters
     * @param parameters    New parameters
     */
    public void setParameters(Map<String, Object> parameters){
        if(parameters != null) {
            this.parameters = parameters;
            init();
        }
    }

    /**
     * Get start filtering time
     * @return Filtering start time
     */
    public LocalTime getFilterFrom() {
        return filterFrom;
    }

    /**
     * Set start filtering time
     * @param filterFrom    New start time
     */
    public void setFilterFrom(LocalTime filterFrom) {
        this.filterFrom = filterFrom;
    }

    /**
     * Get end filtering time
     * @return  End filtering time
     */
    public LocalTime getFilterTo() {
        return filterTo;
    }

    /**
     * Set end filtering time
     * @param filterTo  new end filtering time
     */
    public void setFilterTo(LocalTime filterTo) {
        this.filterTo = filterTo;
    }

    /**
     *  Initialization of plugin - called always after updating parameters list
     */
    public abstract void init();
}
