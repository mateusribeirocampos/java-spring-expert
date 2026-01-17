package entities;

import entities.factories.FinancingFactory;
import org.example.entities.Financing;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

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
}
