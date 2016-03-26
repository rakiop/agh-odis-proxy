package pl.edu.agh.weaiib.is.odis.proxy;

import org.simpleframework.xml.ElementList;

import java.util.List;

public class SerializableList {

    @ElementList(inline = true, entry = "item")
    protected List<String> list;

    public SerializableList(){}

    public SerializableList(List<String> list){
        this.list = list;
    }

    public List<String> getList(){
        return this.list;
    }

}
