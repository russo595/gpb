package org.rustem;

import org.rustem.dto.OperationData;
import org.rustem.service.StatisticServiceImpl;

import java.util.List;

import static org.rustem.service.StatisticServiceImpl.getFullStatisticWithConvertedDate;

public class Task2Application {

    private static final String OFFICES = "operations.txt";
    private StatisticServiceImpl statisticServiceImpl;

    private Task2Application(String[] args) {
        this.statisticServiceImpl = new StatisticServiceImpl(args);
    }

    public static void main(String[] args) {
        new Task2Application(args).run(args.length != 0 && args[0] != null ? args[0] : OFFICES);
    }

    private void run(String resourceFile) {
        List<OperationData> statistic = getFullStatisticWithConvertedDate(resourceFile);
        statisticServiceImpl.groupByDay(statistic);
        statisticServiceImpl.groupBySalesPoint(statistic);
    }
}
