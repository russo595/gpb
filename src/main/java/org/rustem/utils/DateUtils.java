package org.rustem.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Формат вывода даты 01.01.2017 00:00
 */
public class DateUtils {

    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm";

    private static String createRandomDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, -1);

        int YEAR = prevYear.get(Calendar.YEAR);

        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.YEAR, YEAR);
        startDate.set(Calendar.DAY_OF_YEAR, prevYear.getMinimum(Calendar.DAY_OF_YEAR));
        startDate.set(Calendar.HOUR_OF_DAY, prevYear.getMinimum(Calendar.HOUR_OF_DAY));
        startDate.set(Calendar.MINUTE, prevYear.getMinimum(Calendar.MINUTE));
        Date start = startDate.getTime();

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.YEAR, YEAR);
        endDate.set(Calendar.MONTH, prevYear.getMaximum(Calendar.MONTH));
        endDate.set(Calendar.DAY_OF_MONTH, prevYear.getMaximum(Calendar.DAY_OF_MONTH));
        endDate.set(Calendar.HOUR_OF_DAY, prevYear.getMaximum(Calendar.HOUR_OF_DAY));
        endDate.set(Calendar.MINUTE, prevYear.getMaximum(Calendar.MINUTE));
        Date end = endDate.getTime();

        return dateFormat.format(
                new Date(
                        createRandomBetween(start.getTime(), end.getTime())));
    }

    private static long createRandomBetween(long start, long end) {
        return start + (long) (Math.random() * (end - start) + 1);
    }
}
