package pl.edu.agh.weaiib.is.odis.proxy.plugins;

import org.junit.Test;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;

public class FilterTest {

    @Test
    public void settersAreWorkingCorrectly(){
        int priority = 2;
        LocalTime time = LocalTime.now();
        LocalTime time2 = time.plusHours(1);
        Map<String, Object> map = new HashMap<>();

        // Trick to check if parameters are set
        final Map[] setMap = new Map[1];
        Filter filter = new Filter() {
            @Override
            public void init() {
                setMap[0] = this.parameters;
            }
        };

        filter.setFilterFrom(time);
        filter.setFilterTo(time2);
        filter.setPriority(priority);
        filter.setParameters(map);

        // Run test trick
        filter.init();

        assertEquals(filter.getFilterFrom(), time);
        assertEquals(filter.getFilterTo(), time2);
        assertEquals(filter.getPriority(), priority);
        assertEquals(setMap[0], map);

    }

}
