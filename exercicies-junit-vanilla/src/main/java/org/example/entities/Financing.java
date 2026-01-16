package org.example.entities;

public class Financing {

    private Double totalAmount;
    private Double income;
    private Integer months;

    public Financing() {
    }

    public Financing(Double totalAmount, Double income, Integer months) {
        validateFinancing(totalAmount, income, months);
        this.totalAmount = totalAmount;
        this.income = income;
        this.months = months;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        validateFinancing(totalAmount, income, months);
        this.totalAmount = totalAmount;
    }

    public Double getIncome() {
        return income;
    }

    public void setIncome(Double income) {
        validateFinancing(totalAmount, income, months);
        this.income = income;
    }

    public Integer getMonths() {
        return months;
    }

    public void setMonths(Integer months) {
        validateFinancing(totalAmount, income, months);
        this.months = months;
    }

    public double entry() {
        return totalAmount * 0.2;
    }

    public double quota() {
        return (totalAmount - entry())  / months;
    }

    public void validateFinancing(Double totalAmount, Double income, Integer months) {
        if (totalAmount * 0.8 / months > income / 2.0) {
            throw new IllegalArgumentException("The quota cannot be more than a half of the income");
        }
    }
}
