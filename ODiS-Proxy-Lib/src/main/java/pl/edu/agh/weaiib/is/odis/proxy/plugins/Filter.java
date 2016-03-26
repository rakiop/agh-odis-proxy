package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import java.time.LocalTime;
import java.util.Map;

public abstract class Filter {

    private int priority;
    
    private LocalTime filterFrom;

    private LocalTime filterTo;

    protected Map<String, Object> parameters;

    public void setPriority(int priority){
        this.priority = priority;
    }

    public int getPriority(){
        return priority;
    }

    public void setParameters(Map<String, Object> parameters){
        if(parameters != null) {
            this.parameters = parameters;
            init();
        }
    }

    public abstract void init();

    public LocalTime getFilterFrom() {
        return filterFrom;
    }

    public void setFilterFrom(LocalTime filterFrom) {
        this.filterFrom = filterFrom;
    }

    public LocalTime getFilterTo() {
        return filterTo;
    }

    public void setFilterTo(LocalTime filterTo) {
        this.filterTo = filterTo;
    }
}
