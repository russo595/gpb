package org.rustem.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Stream;

@Slf4j
public class RandomUtils {

    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm";
    private static final String OFFICES = "offices.txt";

    private RandomUtils() {}

    /**
     * Формат вывода даты 01.01.2017 00:00
     */
    public static String createRandomDate() {
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

    /**
     * Сумма операции — случайное значение в диапазоне от 10 000,00 до 100 000,00 рублей.
     */
    public static String getRandomSumOperation() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(createRandomBetween(10000d, 100000d));
    }

    public static String getRandomOffice() {
        URL resource = getClassLoader().getResource(OFFICES);
        List<String> listOffices = new ArrayList<>();

        if (resource != null) {
            try (Stream<String> stream = Files.lines(Paths.get(resource.toURI()))) {
                stream.forEach(listOffices::add);
            } catch (URISyntaxException | IOException e) {
                log.error("IOException during to parsing offices.txt", e);
            }
        }

        final long index = createRandomBetween(0L, listOffices.size() - 1L);

        return listOffices.get((int) index);
    }

    public static String getRandomOffice2() {
        List<String> listOffices = null;

        URI uri = null;
        try {
            uri = RandomUtils.class.getClassLoader().getResource(OFFICES).toURI();
        } catch (URISyntaxException e) {
            log.error("Wrong URI", e);
        }
        String[] array = new String[0];
        if (uri != null) {
            array = uri.toString().split("!");
        }

        try (FileSystem fs = FileSystems.newFileSystem(URI.create(array[0]), Collections.emptyMap())) {
            Path path = fs.getPath(array[1]);
            listOffices = Files.readAllLines(path);
        } catch (IOException e) {
            log.error("IOException during to parsing offices.txt", e);
        }

        final long index = createRandomBetween(0L, listOffices.size() - 1L);

        return listOffices.get((int) index);
    }

    private static long createRandomBetween(long start, long end) {
        return start + (long) (Math.random() * (end - start) + 1);
    }

    private static double createRandomBetween(double start, double end) {
        return start + Math.random() * (end - start) + 1;
    }

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
