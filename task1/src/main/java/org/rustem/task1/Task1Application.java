package org.rustem.task1;

import lombok.extern.slf4j.Slf4j;
import org.rustem.task1.utils.RandomUtils;
import org.rustem.task1.utils.RandomUtilsImpl;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

@Slf4j
public class Task1Application {

    private static final String CHARSET_UTF = "UTF-8";
    private RandomUtils randomUtils = new RandomUtilsImpl();

    public static void main(String[] args) {
        new Task1Application().run(args);
    }

    private void run(String[] args) {
        if (args != null && args.length != 0) {
            Integer quantity = Integer.valueOf(args[1]);
            try {
                for (int num = 0; num < quantity; num++) {
                    String randomData = randomUtils.getRandomData(args[0], num);
                    List<String> list = new ArrayList<>();
                    list.add(randomData);
                    Files.write(Paths.get(args[2]), list, Charset.forName(CHARSET_UTF), CREATE, APPEND);
                }
            } catch (IOException e) {
                log.error("Ошибка записи в файл", e);
            }
        } else {
            try {
                for (int i = 0; i < 100; i++) {
                    Files.write(Paths.get("operations.txt"), randomUtils.getRandomData(i).getBytes(), CREATE, APPEND);
                }
            } catch (IOException e) {
                log.error("Ошибка записи в файл", e);
            }
        }
    }
}