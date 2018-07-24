package org.rustem.dto;

import java.math.BigDecimal;
import java.util.Date;

public class OperationData {
    private Date date;
    private String salesPointNumber;
    private String numOperation;
    private BigDecimal sumOperation;

    public Date getDate() {
        return date;
    }

    public OperationData withDate(Date date) {
        this.date = date;
        return this;
    }

    public String getSalesPointNumber() {
        return salesPointNumber;
    }

    public OperationData withSalesPointNumber(String salesPointNumber) {
        this.salesPointNumber = salesPointNumber;
        return this;
    }

    public String getNumOperation() {
        return numOperation;
    }

    public OperationData withNumOperation(String numOperation) {
        this.numOperation = numOperation;
        return this;
    }

    public BigDecimal getSumOperation() {
        return sumOperation;
    }

    public OperationData withSumOperation(BigDecimal sumOperation) {
        this.sumOperation = sumOperation;
        return this;
    }
}
