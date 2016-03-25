package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import java.util.Map;

public abstract class Filter {

    private int priority;

    protected Map<String, String> parameters;

    public void setPriority(int priority){
        this.priority = priority;
    }

    public int getPriority(){
        return priority;
    }

    public void setParameters(Map<String, String> parameters){
        if(parameters != null) {
            this.parameters = parameters;
            init();
        }
    }

    public abstract void init();
}
