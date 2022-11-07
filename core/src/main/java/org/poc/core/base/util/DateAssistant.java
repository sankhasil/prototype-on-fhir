package org.poc.core.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Utils class to parse date type objects.
 *
 * <p>SD Global Technologies. Copyright &copy; 2019. All rights reserved.
 *
 * @author Karthik Chandra
 */
public class DateAssistant {
    /**
     * This class need not be instantiated.
     */
    private DateAssistant() {
    }

    /**
     * Deserialize to {@link Date} object.
     *
     * @param date of {@link Object}
     * @return a {@link Date} instance.
     */
    public static Date deserialize(final Object date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        Date convertedDate = new Date();

        try {
            if (date instanceof String) {
                convertedDate = format.parse((String) date);
            } else if (date instanceof Date) {
                convertedDate = (Date) date;
            }
        } catch (ParseException e) {
            // TODO Add logger
            e.printStackTrace();
        }

        return convertedDate;
    }

    /**
     * Get {@link LocalDateTime} from given Epoch Mills.
     *
     * @param epochMills a valid epoch time.
     * @return parsed {@link LocalDateTime}
     */
    public static LocalDateTime getLocalDateTime(final Long epochMills) {
        final Instant instant = Instant.ofEpochSecond(epochMills);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Get {@link Date} from given Epoch seconds.
     *
     * @param epochSeconds valid epoch seconds.
     * @return {@link Date} object.
     */
    public static Date getDate(final Long epochSeconds) {
        return new Date(epochSeconds * 1000);
    }

    /**
     * Checks if the given date is between required date range.
     *
     * @param given     is the given date from transaction.
     * @param startDate is a valid start date.
     * @param endDate   is a valid end date.
     * @return True if date in range else false.
     */
    public static boolean isDateInBound(final Date given, final Date startDate, final Date endDate) {
        return given.after(startDate) && given.before(endDate);
    }

    /**
     * Get today's start date time.
     *
     * @return a {@link Date} object.
     */
    public static Date getTodayStartTime() {
        final Instant today = Instant.now();
        final ZoneId zoneId = ZoneId.systemDefault();

        return Date.from(ZonedDateTime.ofInstant(today, zoneId)
                .toLocalDate()
                .atStartOfDay(zoneId)
                .toInstant());
    }

    /**
     * Get tomorrow's end date time.
     *
     * @return a {@link Date} object.
     */
    public static Date getTomorrowStartTime() {
        final Instant today = Instant.now();
        final ZoneId zoneId = ZoneId.systemDefault();

        return Date.from(
                ZonedDateTime.ofInstant(today, zoneId).plusDays(1)
                        .toLocalDate()
                        .atStartOfDay(zoneId)
                        .toInstant());
    }

    /**
     * Get epoch seconds for given date.
     *
     * @param date a valid {@link Date}
     * @return the epoch seconds.
     */
    public static <T> Long getEpochSeconds(final T date) {
        final Date date1 = (Date) date;
        return date1.getTime() / 1000;
    }

    /**
     * Gets the start date with time 00:00:00
     *
     * @param start a date instance.
     * @return date instance that was sent as parameter with time truncated.
     * @author Karthik Chandra
     */
    public static Date getDayStart(Date start) {
        if (start == null) {
            start = new Date();
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(start);
        calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Kuala_Lumpur")));
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        return calendar.getTime();
    }

    /**
     * Gets the end date with time 23:59:59
     *
     * @param end a date instance.
     * @return date instance that was sent as parameter with time truncated.
     * @author Karthik Chandra
     */
    public static Date getDayEnd(Date end) {
        if (end == null) {
            end = new Date();
        }
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(end);
        calendar.setTimeZone(TimeZone.getTimeZone(ZoneId.of("Asia/Kuala_Lumpur")));
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);

        return calendar.getTime();
    }
}
