## Генерация тестовых данных [![Build Status](https://travis-ci.org/russo595/gpb.svg?branch=develop)](https://travis-ci.org/russo595/gpb)

 * Программа, которая создает текстовый файл, содержащий дату, время, номер точки продаж, номер операции и сумму операции.
 
 * Программе в качестве параметров передается имя файла со списком точек продаж, коли-чество операций и файл, куда записать сгенерированные данные.
 
 * Пример запуска программы: java -jar task1.jar offices.txt 90000 operations.txt
 
## Группировка данных

 * Программа, считает статистику по операциям.
 
 * Данные об операциях находятся в файле, который сгенерирован в task1.jar.
 
 * Программа подсчитывает сумму всех операций за каждый день и суммы всех операций в каждой точке продаж.
   
 * Программе в качестве параметров передаются имя файла с операциями, имя файла со статистикой по датам, имя файла со статистикой по точкам продаж.
   
 * Статистика по датам отсортирована по возрастанию дат.
 
 * Статистика по точкам продаж отсортирована по убыванию суммы.
 
 * Пример запуска программы:
   java -jar task2.jar operations.txt sums-by-dates.txt sums-by-offices.txt