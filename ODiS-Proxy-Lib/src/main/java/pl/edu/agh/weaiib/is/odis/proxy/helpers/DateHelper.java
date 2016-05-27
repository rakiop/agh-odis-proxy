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

/**
 * Helper to handle with LocalTime class
 */
public class DateHelper {

    /**
     * Main application logger
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(DateHelper.class);

    /**
     * Pattern for hours and minutes
     */
    private static final SimpleDateFormat hoursMinutesParser = new SimpleDateFormat("HH:mm");

    /**
     * Pattern for hours, minutes and seconds
     */
    private static final SimpleDateFormat hoursMinutesSecondsParser = new SimpleDateFormat("HH:mm:ss");

    /**
     * Parse string value to LocalTime object
     * @param time      String value
     * @return          LocalTime object or null
     */
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

    /**
     * Check if current time is between given local times
     * @param from      Left border
     * @param to        Right border
     * @return          Is current between?
     */
    public static boolean currentTimeIsBetween(LocalTime from, LocalTime to){
        LocalTime current = LocalTime.now();

        if(from == null || to == null) return true;

        return (current.isAfter(from) && current.isBefore(to))
                || current.equals(from) || current.equals(to);

    }

    /**
     * Is difference between dates is more that diff nanos
     * @param date1     Date 1
     * @param date2     Date 2
     * @param diff      Difference nanos
     * @return          True or not?
     */
    public static boolean differenceIsMoreThen(Date date1, Date date2, long diff){
        return date1.getTime() - date2.getTime() > diff;
    }

}
