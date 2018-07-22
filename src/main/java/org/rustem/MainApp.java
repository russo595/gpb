package org.rustem;

import org.rustem.utils.RandomUtils;

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

public class MainApp {
    public static void main(String[] args) {

        if (args != null) {
            System.out.println(RandomUtils.getRandomOffice2());
        } else {
            System.out.println(RandomUtils.getRandomOffice());
        }
    }
}