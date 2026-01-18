package entities;

import entities.factories.FinancingFactory;
import org.example.entities.Financing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


public class FinancingTests {

    @Test
    public void constructorShouldCreateObjectWhenValidData() {

        // Arrange
        double expectedTotalAmount = 200000.0;
        double expectedIncome = 4000.0;
        Integer expectedMonths = 80;

        //Act
        Financing fin1 = new Financing(expectedTotalAmount, expectedIncome, expectedMonths);
        Financing fin2 = FinancingFactory.createDefaultValidFinancing();

        // Assert
        Assertions.assertEquals(expectedTotalAmount, fin1.getTotalAmount());
        Assertions.assertEquals(expectedIncome, fin1.getIncome());
        Assertions.assertEquals(expectedMonths, fin1.getMonths());

        Assertions.assertEquals(100000.0, fin2.getTotalAmount());
        Assertions.assertEquals(2000.0, fin2.getIncome());
        Assertions.assertEquals(80, fin2.getMonths());
    }

    // Constructor
    @Test
    public void constructorShouldReleaseExceptionWhenInvalidData() {

        // Arrange
        double expectedTotalAmount = 200000.0;
        double expectedIncome = 4000.0;
        Integer expectedMonths = 20;

        double[] data = FinancingFactory.getInvalidFinancingData();

        // Act
        Financing fin3;

        // Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Financing(expectedTotalAmount, expectedIncome, expectedMonths);
        });

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Financing(100000.0, 2000.0, 20);
        });
    }

    // setTotalAmount
    @Test
    public void updateShouldTotalAmountWhenValidData() {
        // Arrange
        double expectedTotalAmount = 90000.0;
        // Act
        Financing fin4 = FinancingFactory.createDefaultValidFinancing();

        fin4.setTotalAmount(expectedTotalAmount);

        // Assert
        Assertions.assertEquals(expectedTotalAmount, fin4.getTotalAmount());

    }

    @Test
    public void exceptionShouldTotalAmountWhenInvalidData() {
        // Arrange
        double updatedTotalAmount = 200000.0;
        // Act
        Financing fin4 = FinancingFactory.createDefaultValidFinancing();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            fin4.setTotalAmount(updatedTotalAmount);
        });
    }

    //setIncome
    @Test
    public void updateShouldIncomeWhenValidData() {
        double updateIncome = 5000.0;

        Financing fin5 = FinancingFactory.createDefaultValidFinancing();
        fin5.setIncome(updateIncome);

        Assertions.assertEquals(updateIncome, fin5.getIncome());
    }

    @Test
    public void exceptionShouldIncomeWhenInvalidData() {
        double updateIncome = 1000.0;

        Financing fin5 = FinancingFactory.createDefaultValidFinancing();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            fin5.setIncome(updateIncome);
        });
    }

    // SetMonths
    @Test
    public void updateShouldMonthsWhenValidData() {
        Integer updatedMonths = 100;

        Financing fin6 = FinancingFactory.createDefaultValidFinancing();
        fin6.setMonths(updatedMonths);

        Assertions.assertEquals(updatedMonths, fin6.getMonths());

    }

    @Test
    public void exceptionShouldMonthWhenInvalidData() {
        Integer updatedMonths = 30;

        Financing fin7 = FinancingFactory.createDefaultValidFinancing();

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
           fin7.setMonths(updatedMonths);
        });
    }

    // entry()
    @Test
    public void entryShouldReturn20PercentOfTotalAmount() {
        // Arrange
        Financing fin = FinancingFactory.createDefaultValidFinancing();
        double expectedEntry = 20000.0; // 100000 * 0.2 = 20000

        // Act
        double actualEntry = fin.entry();

        // Assert
        Assertions.assertEquals(expectedEntry, actualEntry);
    }

    // quota()
    @Test
    public void quotaShouldReturnCorrectMonthlyPayment() {
        // Arrange
        Financing fin = FinancingFactory.createDefaultValidFinancing();
        double expectedQuota = 1000.0; // (100000 - 20000) / 80 = 1000

        // Act
        double actualQuota = fin.quota();

        // Assert
        Assertions.assertEquals(expectedQuota, actualQuota);
    }
}
