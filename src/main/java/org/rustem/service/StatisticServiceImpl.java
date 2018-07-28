package org.rustem.service;

import org.rustem.dto.OperationData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;
import static org.rustem.utils.DateUtils.stringDate;
import static org.rustem.utils.DateUtils.toDate;

public class StatisticServiceImpl implements StatisticService {

    private static final Logger log = LoggerFactory.getLogger(StatisticServiceImpl.class);

    private static final String EMPTY = "";
    private static final String REGEX = "\\|";
    private static final String REGEX_SYMB = "\n";
    private static final String REGEX_TAB = "\t";
    private static final String COMMA = ",";
    private static final String DOT = ".";
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String ERROR_PARSING_DATE = "Ошибка парсинга даты";
    private static final String CHARSET_UTF = "UTF-8";
    private static final String IO_EXCEPTION = "Ошибка записи в файл";
    private String sumsByDatesTXT;
    private String sumsByOfficesTXT;

    private static OperationData data;

    public StatisticServiceImpl(String[] args) {
        sumsByDatesTXT = args.length > 1 && args[1] != null ? args[1] : "sums-by-dates.txt";
        sumsByOfficesTXT = args.length > 2 && args[2] != null ? args[2] : "sums-by-offices.txt";
    }

    /**
     * Группировка по датам и вывод отсортированной статистики по возрастанию дат
     *
     * @param statistic список с полной статистикой операций
     */
    @Override
    public void groupByDay(List<OperationData> statistic) {
        Map<Date, BigDecimal> dataGroupByDay = statistic.stream()
                .collect(Collectors.groupingBy(
                        OperationData::getDate,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                OperationData::getSumOperation,
                                BigDecimal::add)))
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        TreeMap<Date, BigDecimal> sortedData = new TreeMap<>(dataGroupByDay);

        List<String> listSumOperationsByDay = sortedData.entrySet().stream().
                map(dateDoubleEntry -> String.format("%s\t|\t%s", stringDate(dateDoubleEntry.getKey()), dateDoubleEntry.getValue().toString()))
                .collect(Collectors.toList());

        try {
            Files.write(Paths.get(sumsByDatesTXT), listSumOperationsByDay, Charset.forName(CHARSET_UTF), CREATE, APPEND);
            log.info("Data uploaded to file {}", sumsByDatesTXT);
        } catch (IOException e) {
            log.error(IO_EXCEPTION, e);
        }
    }

    /**
     * Группировка по точкам продаж и вывод отсортированной статистики по убыванию суммы
     *
     * @param statistic список с полной статистикой операций
     */
    public void groupBySalesPoint(List<OperationData> statistic) {
        Map<String, BigDecimal> dataGroupByDay = statistic.stream()
                .collect(Collectors.groupingBy(
                        OperationData::getSalesPointNumber,
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                OperationData::getSumOperation,
                                BigDecimal::add)))
                .entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        List<String> listSumOperationsByDay = dataGroupByDay.entrySet().stream().
                map(dateDoubleEntry -> String.format("%s\t|\t%s", dateDoubleEntry.getKey(), dateDoubleEntry.getValue().toString()))
                .collect(Collectors.toList());

        try {
            Files.write(Paths.get(sumsByOfficesTXT), listSumOperationsByDay, Charset.forName(CHARSET_UTF), CREATE, APPEND);
            log.info("Data uploaded to file {}", sumsByOfficesTXT);
        } catch (IOException e) {
            log.error(IO_EXCEPTION, e);
        }
    }

    /**
     * Метод для получения полной статистики
     *
     * @param resourceFile ресурный файл где хранится полная статистика
     * @return список состоящий из объектов OperationData
     */
    private static List<OperationData> getFullStatistic(String resourceFile) {
        List<OperationData> operationDataList = new ArrayList<>();

        try (Stream<String> lines = Files.lines(Paths.get(resourceFile))) {
            operationDataList = lines.filter(s -> !s.equals(EMPTY)).map(mapToOperationDataFromFile())
                    .peek(operationDataWithoutSymbols())
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("Ошибка чтения файла", e);
        }
        return operationDataList;
    }

    /**
     * Метод для перекладки данных из файла в объект OperationData
     */
    private static Function<String, OperationData> mapToOperationDataFromFile() {
        return s -> {
            data = new OperationData();
            String[] split = s.split(REGEX);

            return data.withDate(toDate(split[0]))
                    .withSalesPointNumber(split[1])
                    .withNumOperation(split[2])
                    .withSumOperation(new BigDecimal(split[3].replace(REGEX_TAB, EMPTY).replace(COMMA, DOT)));
        };
    }

    /**
     * Метод для удаления лишних символов при парсинге строки ("|", "\n", "\t")
     */
    private static Consumer<OperationData> operationDataWithoutSymbols() {
        return operationData -> operationData
                .withDate(operationData.getDate())
                .withSalesPointNumber(operationData.getSalesPointNumber().replaceAll(REGEX_SYMB, EMPTY).replaceAll(REGEX_TAB, EMPTY))
                .withNumOperation(operationData.getNumOperation().replaceAll(REGEX_SYMB, EMPTY).replaceAll(REGEX_TAB, EMPTY))
                .withSumOperation(operationData.getSumOperation());
    }

    /**
     * Метод для получения полной статистики имеющий формат даты dd.MM.yyyy
     *
     * @param resourceFile ресурный файл где хранится полная статистика
     * @return список состоящий из объектов OperationData с форматом даты dd.MM.yyyy
     */
    public static List<OperationData> getFullStatisticWithConvertedDate(String resourceFile) {
        return getFullStatistic(resourceFile).stream()
                .peek(operationData -> {
                    try {
                        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
                        Date date = format.parse(stringDate(operationData.getDate()));
                        operationData.withDate(toDate(format.format(date)));
                    } catch (ParseException e) {
                        log.error(ERROR_PARSING_DATE, e);
                    }
                }).collect(Collectors.toList());
    }
}
