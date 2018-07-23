package org.rustem.service;

import lombok.extern.slf4j.Slf4j;
import org.rustem.dto.OperationData;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class StatisticService {

    private static final String EMPTY = "";
    private static final String REGEX = "\\|";
    private static final String REGEX_SYMB = "\n";
    private static final String REGEX_TAB = "\t";

    private OperationData data;

    public List<OperationData> getFullStatistic() {
        String path = "./operations.txt";
        List<OperationData> operationDataList = null;

        try (FileInputStream fis = new FileInputStream(path);
             BufferedReader in = new BufferedReader(new InputStreamReader(fis))) {
            Stream<String> lines = in.lines();
            operationDataList = lines.map(mapToOperationDataFromFile())
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
            data.withDate(split[0]).withSalesPointNumber(split[1]).withNumOperation(split[2]).withSumOperation(split[3]);
            return data;
        };
    }

    private Consumer<OperationData> operationDataWithoutSymbols() {
        return operationData -> operationData
                .withDate(operationData.getDate().replaceAll(REGEX_SYMB, EMPTY).replaceAll(REGEX_TAB, EMPTY))
                .withSalesPointNumber(operationData.getSalesPointNumber().replaceAll(REGEX_SYMB, EMPTY).replaceAll(REGEX_TAB, EMPTY))
                .withNumOperation(operationData.getNumOperation().replaceAll(REGEX_SYMB, EMPTY).replaceAll(REGEX_TAB, EMPTY))
                .withSumOperation(operationData.getSumOperation().replaceAll(REGEX_SYMB, EMPTY).replaceAll(REGEX_TAB, EMPTY));
    }
}
