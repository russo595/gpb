package org.rustem.task1.utils;

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
public class RandomUtilsImpl implements RandomUtils {

    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm";
    private static final String OFFICES = "offices.txt";

    @Override
    public String getRandomData(int i) {
        return String.format("%s\t |\t%s\t |\t%s\t |\t%s%n", createRandomDate(), getRandomOffice(), i, getRandomSumOperation());
    }

    @Override
    public String getRandomData(String file, int num) {
        return String.format("%s\t |\t%s\t |\t%s\t |\t%s%n", createRandomDate(), getRandomOffice(file), num, getRandomSumOperation());
    }

    /**
     * Формат вывода даты 01.01.2017 00:00
     */
    @Override
    public String createRandomDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar prevYear = Calendar.getInstance();
        prevYear.add(Calendar.YEAR, -1);

        int year = prevYear.get(Calendar.YEAR);

        Calendar startDate = Calendar.getInstance();
        startDate.set(Calendar.YEAR, year);
        startDate.set(Calendar.DAY_OF_YEAR, prevYear.getMinimum(Calendar.DAY_OF_YEAR));
        startDate.set(Calendar.HOUR_OF_DAY, prevYear.getMinimum(Calendar.HOUR_OF_DAY));
        startDate.set(Calendar.MINUTE, prevYear.getMinimum(Calendar.MINUTE));
        Date start = startDate.getTime();

        Calendar endDate = Calendar.getInstance();
        endDate.set(Calendar.YEAR, year);
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
    public String getRandomSumOperation() {
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        return numberFormat.format(createRandomBetween(10000d, 100000d));
    }

    public String getRandomOffice() {
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

    public String getRandomOffice(String file) {
        List<String> listOffices = null;

        URI uri = null;
        try {
            URL resource = RandomUtilsImpl.class.getClassLoader().getResource(file);
            if (resource != null) {
                uri = resource.toURI();
            }
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
