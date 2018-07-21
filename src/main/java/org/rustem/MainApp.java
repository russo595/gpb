package org.rustem;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Напишите программу, которая создает текстовый файл, содержащий дату, время, номер точки продаж, номер операции и сумму операции.
 * Время должно выбираться случайно в диапазоне за предыдущий год. Т. е. если програм-ма запущенна 02.06.2018, то время операции может быть от 01.01.2017 00:00 до 01.01.2018 00:00.
 * Номер точки продаж должен случайным образом выбирается из заранее подготовленно-го списка.
 * Список точек продаж хранится в текстовом файле, на одной строке одна точка продаж.
 * Сумма операции — случайное значение в диапазоне от 10 000,00 до 100 000,00 рублей.
 * Программе в качестве параметров передается имя файла со списком точек продаж, коли-чество операций и файл, куда записать сгенерированные данные.
 * Пример запуска программы:
 * java -jar task1.jar offices.txt 90000 operations.txt
 */

@Slf4j
public class MainApp {

    private static final String OFFICES = "offices.txt";

    public static void main(String[] args) {
        MainApp app = new MainApp();
        URL resource = app.getClassLoader().getResource(OFFICES);
        List<String> listOffices = new ArrayList<>();
        if (resource != null) {

            try (Stream<String> stream = Files.lines(Paths.get(resource.getPath()))) {
                stream.forEach(listOffices::add);
            } catch (IOException e) {
                log.error("IOException during to parsing offices.txt", e);
            }
        }
    }

    public ClassLoader getClassLoader() {
        return this.getClass().getClassLoader();
    }
}

