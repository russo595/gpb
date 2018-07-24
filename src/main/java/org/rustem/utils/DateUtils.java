package org.rustem.utils;

import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
public class DateUtils {

    private DateUtils() {
    }

    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String ERROR_PARSING_DATE = "Ошибка парсинга даты";

    public static String stringDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        return format.format(date);
    }

    public static Date toDate(String date) {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        Date parsedDate = null;
        try {
            parsedDate = format.parse(date.replace("\t", ""));
        } catch (ParseException e) {
            log.error(ERROR_PARSING_DATE, e);
        }
        return parsedDate;
    }
}
