package pl.edu.agh.weaiib.is.odis.proxy;

import org.junit.Test;

import java.util.LinkedList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

public class SerializableListTest {

    @Test
    public void constructorIsSettingList(){
        List<String> list = new LinkedList<>();
        SerializableList serializableList = new SerializableList(list);

        assertEquals(list, serializableList.getList());
    }

}
