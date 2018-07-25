package org.rustem;

import org.rustem.dto.OperationData;
import org.rustem.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

import static org.rustem.service.StatisticServiceImpl.getFullStatisticWithConvertedDate;

@SpringBootApplication
public class Task2Application {

    private static final String OFFICES = "operations.txt";


    @Autowired
    private StatisticService statisticService;

    public static void main(String[] args) {
        SpringApplication.run(Task2Application.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            List<OperationData> statistic = getFullStatisticWithConvertedDate(args.length != 0 && args[0] != null ? args[0] : OFFICES);
            statisticService.groupByDay(statistic);
            statisticService.groupBySalesPoint(statistic);
        };
    }
}
