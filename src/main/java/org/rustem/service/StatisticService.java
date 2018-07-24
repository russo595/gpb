package org.rustem.service;

import org.rustem.dto.OperationData;

import java.util.List;

public interface StatisticService {
    void groupByDay(List<OperationData> statistic);

    void groupBySalesPoint(List<OperationData> statistic);
}
