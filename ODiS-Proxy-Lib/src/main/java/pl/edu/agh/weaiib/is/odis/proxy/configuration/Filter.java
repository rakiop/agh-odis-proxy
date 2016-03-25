package pl.edu.agh.weaiib.is.odis.proxy.configuration;

import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="filter")
public class Filter {

    private String filterName;

    private FilterPlace place;

    private int priority;

    private Map<String, String> parameters;

    public Filter(){
        parameters = new HashMap<String, String>();
    };

    public Filter(String filterName, FilterPlace place, int priority){
        this();
        this.filterName = filterName;
        this.place = place;
        this.priority = priority;
    }

    public void addParameter(String key, String value){
        parameters.put(key, value);
    }

    @XmlElement(name="class")
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

    @XmlElementWrapper(name="parameters")
    @XmlElement(name="parameter")
    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
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
