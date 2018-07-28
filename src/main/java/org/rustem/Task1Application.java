package org.rustem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static org.rustem.utils.RandomUtils.getRandomData;

public class Task1Application {

    private static final Logger log = LoggerFactory.getLogger(Task1Application.class);

    private static final String CHARSET_UTF = "UTF-8";
    private static final String OPERATIONS = "operations.txt";
    private static final String IO_EXCEPTION = "Ошибка записи в файл";

    public static void main(String[] args) {
        new Task1Application().run(args);
    }

    private void run(String[] args) {
        if (args != null && args.length != 0) {
            Integer quantity = Integer.valueOf(args[1]);
            try { // блок для запуска программы из jar файла
                List<String> list = new ArrayList<>();

                for (int num = 0; num < quantity; num++) {
                    list.add(getRandomData(args[0], num));
                }

                Files.write(Paths.get(args[2]), list, Charset.forName(CHARSET_UTF), CREATE, APPEND);
                log.info("Data uploaded to file {}", args[2]);
            } catch (IOException e) {
                log.error(IO_EXCEPTION, e);
            }
        } else {
            try { // блок для того что бы зупустить программу в IntelliJ IDEA
                for (int i = 0; i < 900; i++) {
                    Files.write(Paths.get(OPERATIONS), getRandomData(i).getBytes(), CREATE, APPEND);
                }
                log.info("Data uploaded to file");
            } catch (IOException e) {
                log.error(IO_EXCEPTION, e);
            }
        }
    }
}