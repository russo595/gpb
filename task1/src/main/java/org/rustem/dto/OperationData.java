package org.rustem.dto;

public class OperationData {
    private String date;
    private String salesPointNumber;
    private String numOperation;
    private String sumOperation;

    public String getDate() {
        return date;
    }

    public OperationData withDate(String date) {
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

    public String getSumOperation() {
        return sumOperation;
    }

    public OperationData withSumOperation(String sumOperation) {
        this.sumOperation = sumOperation;
        return this;
    }
}
