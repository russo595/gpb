package org.rustem.service;

import lombok.extern.slf4j.Slf4j;
import org.rustem.dto.OperationData;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
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

@Slf4j
public class StatisticServiceImpl implements StatisticService {

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
    private String operationTXT;
    private String sumsByDatesTXT;
    private String sumsByOfficesTXT;

    private OperationData data;

    public StatisticServiceImpl(String[] args) {
        operationTXT = args.length != 0 && args[0] != null ? args[0] : "operations.txt";
        sumsByDatesTXT = args.length != 0 && args[1] != null ? args[1] : "sums-by-dates.txt";
        sumsByOfficesTXT = args.length != 0 && args[2] != null ? args[2] : "sums-by-offices.txt";
    }

    @Override
    public void groupByDay() {
        Map<Date, BigDecimal> dataGroupByDay = convertedDate().stream()
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
        } catch (IOException e) {
            log.error(IO_EXCEPTION, e);
        }
    }

    public void groupBySalesPoint() {
        Map<String, BigDecimal> dataGroupByDay = convertedDate().stream()
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
            Files.write(Paths.get(sumsByOfficesTXT), listSumOperationsByDay, Charset.forName("windows-1251"), CREATE, APPEND);
        } catch (IOException e) {
            log.error(IO_EXCEPTION, e);
        }
    }

    private List<OperationData> getFullStatistic() {
        String path = operationTXT;
        List<OperationData> operationDataList = null;

        try (FileInputStream fis = new FileInputStream(path);
             BufferedReader in = new BufferedReader(new InputStreamReader(fis))) {
            Stream<String> lines = in.lines();
            operationDataList = lines.filter(s -> !s.equals(EMPTY)).map(mapToOperationDataFromFile())
                    .peek(operationDataWithoutSymbols())
                    .collect(Collectors.toList());

        } catch (IOException e) {
            log.error("Ошибка чтения файла", e);
        }
        return operationDataList;
    }

    private Function<String, OperationData> mapToOperationDataFromFile() {
        return s -> {
            data = new OperationData();
            String[] split = s.split(REGEX);

            return data.withDate(toDate(split[0]))
                    .withSalesPointNumber(split[1])
                    .withNumOperation(split[2])
                    .withSumOperation(new BigDecimal(split[3].replace(REGEX_TAB, EMPTY).replace(COMMA, DOT)));
        };
    }

    private Consumer<OperationData> operationDataWithoutSymbols() {
        return operationData -> operationData
                .withDate(operationData.getDate())
                .withSalesPointNumber(operationData.getSalesPointNumber().replaceAll(REGEX_SYMB, EMPTY).replaceAll(REGEX_TAB, EMPTY))
                .withNumOperation(operationData.getNumOperation().replaceAll(REGEX_SYMB, EMPTY).replaceAll(REGEX_TAB, EMPTY))
                .withSumOperation(operationData.getSumOperation());
    }

    private List<OperationData> convertedDate() {
        return getFullStatistic().stream()
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