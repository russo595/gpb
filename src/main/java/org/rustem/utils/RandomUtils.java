package org.rustem.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Stream;

public class RandomUtils {

    private static final Logger log = LoggerFactory.getLogger(RandomUtils.class);

    private RandomUtils() {
    }

    private static final String DATE_FORMAT = "dd.MM.yyyy HH:mm";
    private static final String OFFICES = "offices.txt";

    public static String getRandomData(int i) {
        return String.format("%s\t |\t%s\t |\t%s\t |\t%s%n", createRandomDate(), getRandomOffice(), i, getRandomSumOperation());
    }

    public static String getRandomData(String file, int num) {
        return String.format("%s\t |\t%s\t |\t%s\t |\t%s%n", createRandomDate(), getRandomOffice(file), num, getRandomSumOperation());
    }

    /**
     * Формат вывода даты 01.01.2017 00:00
     */
    private static String createRandomDate() {
        LocalDateTime localStart = LocalDateTime.of(LocalDateTime.now().getYear() - 1, Month.JANUARY, 1, 0, 0);
        LocalDateTime localEnd = LocalDateTime.of(LocalDateTime.now().getYear(), Month.JANUARY, 1, 0, 0);

        long randomBetween = createRandomBetween(
                localStart.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli(),
                localEnd.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
        );
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(randomBetween), TimeZone.getDefault().toZoneId());

        return localDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

    /**
     * Сумма операции — случайное значение в диапазоне от 10000,00 до 100000,00 рублей.
     */
    private static String getRandomSumOperation() {
        return String.format("%.2f", createRandomBetween(10000d, 100000d));
    }

    private static String getRandomOffice() {
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

    private static String getRandomOffice(String file) {
        List<String> listOffices = new ArrayList<>();

        URI uri = null;
        try {
            URL resource = getClassLoader().getResource(file);
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

        return listOffices.get(createRandomBetween(listOffices.size() - 1));
    }

    private static long createRandomBetween(long start, long end) {
        return start + (long) (Math.random() * (end - start) + 1);
    }

    private static int createRandomBetween(int end) {
        return new Random().nextInt(end + 1);
    }

    private static double createRandomBetween(double start, double end) {
        return start + Math.random() * (end - start) + 1;
    }

    private static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }
}
