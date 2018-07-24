package org.rustem;

import org.rustem.service.StatisticServiceImpl;

public class Task2Application {

    private StatisticServiceImpl statisticServiceImpl;

    private Task2Application(String[] args) {
        this.statisticServiceImpl = new StatisticServiceImpl(args);
    }

    public static void main(String[] args) {
        new Task2Application(args).run();
    }

    private void run() {
        statisticServiceImpl.groupByDay();
        statisticServiceImpl.groupBySalesPoint();
    }
}
