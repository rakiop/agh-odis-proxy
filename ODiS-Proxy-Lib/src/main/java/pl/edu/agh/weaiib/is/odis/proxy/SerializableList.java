package pl.edu.agh.weaiib.is.odis.proxy;

import org.simpleframework.xml.ElementList;

import java.util.List;

/**
 * Wrapper to serialize list in XML
 */
public class SerializableList {

    /**
     * Real list to serialize
     */
    @ElementList(inline = true, entry = "item")
    protected List<String> list;

    /**
     * Default construction for serialization
     */
    public SerializableList(){}

    /**
     * Constructor to set real list
     * @param list  Real list
     */
    public SerializableList(List<String> list){
        this.list = list;
    }

    /**
     * Get real list
     * @return  Real list
     */
    public List<String> getList(){
        return this.list;
    }

}
