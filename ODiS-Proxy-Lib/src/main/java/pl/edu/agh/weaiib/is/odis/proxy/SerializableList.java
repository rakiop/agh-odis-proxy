package pl.edu.agh.weaiib.is.odis.proxy;

import org.simpleframework.xml.ElementList;

import java.util.List;

public class SerializableList<T> {

    @ElementList(inline = true, entry = "item")
    protected List<T> list;

    public SerializableList(){}

    public SerializableList(List<T> list){
        this.list = list;
    }

    public List<T> getList(){
        return this.list;
    }

}
