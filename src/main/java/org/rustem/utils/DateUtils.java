package org.rustem.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.regex.Pattern;

public class DateUtils {

    private static final String REGEX_TAB = "\t";
    private static final String EMPTY = "";

    private DateUtils() {
    }

    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String DATE_TIME_FORMAT = "dd.MM.yyyy HH:mm";
    private static final String REGEX = "\\d{2}.\\d{2}.\\d{4}";

    public static String stringDate(LocalDate date) {
        return Optional.ofNullable(date)
                .orElseThrow(() -> new NullPointerException("Date is not must be null"))
                .format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    public static LocalDate toDate(String date) {
        date = Optional.ofNullable(date)
                .orElseThrow(() -> new NullPointerException("Date is not must be null"))
                .replace(REGEX_TAB, EMPTY).trim();
        DateTimeFormatter formatter;
        Pattern p = Pattern.compile(REGEX);
        if (p.matcher(date).matches()) {
            formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        } else {
            formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
        }

        return LocalDate.parse(date, formatter);
    }
}
