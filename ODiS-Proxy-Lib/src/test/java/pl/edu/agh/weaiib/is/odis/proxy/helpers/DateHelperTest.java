package pl.edu.agh.weaiib.is.odis.proxy.helpers;

import org.junit.Test;

import java.time.LocalTime;

import static junit.framework.Assert.*;

public class DateHelperTest {

    @Test
    public void parseTime(){
        String time = "12:01";
        LocalTime dt = DateHelper.parseTime(time);

        assertEquals(dt.getHour(), 12);
        assertEquals(dt.getMinute(), 1);
    }

    @Test
    public void parseTimeWithSeconds(){
        String time = "12:01:30";
        LocalTime dt = DateHelper.parseTime(time);

        assertEquals(dt.getHour(), 12);
        assertEquals(dt.getMinute(), 1);
        assertEquals(dt.getSecond(), 30);
    }

    @Test
    public void parseInvalidTime(){
        String time = "12AM";
        LocalTime dt = DateHelper.parseTime(time);

        assertNull(dt);
    }

    @Test
    public void isCurrentBetween(){
        LocalTime before = LocalTime.now().minusHours(1);
        LocalTime after = LocalTime.now().plusHours(1);

        assertTrue(DateHelper.currentTimeIsBetween(before, after));

        before = LocalTime.now();
        assertTrue(DateHelper.currentTimeIsBetween(before, after));

        before = LocalTime.now().minusHours(1);
        after = LocalTime.now();
        assertTrue(DateHelper.currentTimeIsBetween(before, after));

        before = LocalTime.now().plusHours(1);
        after = before.plusMinutes(1);
        assertFalse(DateHelper.currentTimeIsBetween(before, after));

        before = LocalTime.now().minusHours(1);
        after = LocalTime.now().minusHours(1);
        assertFalse(DateHelper.currentTimeIsBetween(before, after));

    }


}
