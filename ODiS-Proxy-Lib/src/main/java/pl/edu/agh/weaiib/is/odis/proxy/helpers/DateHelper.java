package pl.edu.agh.weaiib.is.odis.proxy.helpers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;

public class DateHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(DateHelper.class);

    private static final SimpleDateFormat hoursMinutesParser = new SimpleDateFormat("HH:mm");
    private static final SimpleDateFormat hoursMinutesSecondsParser = new SimpleDateFormat("HH:mm:ss");

    public static LocalTime parseTime(String time){
        Date date = null;
        try {
            date = hoursMinutesSecondsParser.parse(time);
        } catch (ParseException e) {
            try {
                date = hoursMinutesParser.parse(time);
            } catch (ParseException e1) {
                LOGGER.warn(String.format("Could not parse time: %s", time));
            }
        }

        if(date == null)
            return null;

        Instant instant = Instant.ofEpochMilli(date.getTime());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalTime();
    }

    public static boolean currentTimeIsBetween(LocalTime from, LocalTime to){
        LocalTime current = LocalTime.now();

        if(from == null || to == null) return true;

        return (current.isAfter(from) && current.isBefore(to))
                || current.equals(from) || current.equals(to);

    }

}
