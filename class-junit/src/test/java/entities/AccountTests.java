package entities;

import org.example.entities.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTests {

    @Test
    public void depositShouldIncreaseBalanceWhenPositiveAmount() {

        double amount = 200.0;
        double expectedValue = 196.0;
        Account acc = new Account(1L, 0.0);

        acc.deposit(amount);

        Assertions.assertEquals(expectedValue, acc.getBalance());
    }

    @Test
    public void withdrawShouldDecreaseBalanceWhenPositiveAmount() {
        double expectedValue = 100.0;

        Account acc = new Account(1L, 200.0);

        acc.withdraw(100.0);

        Assertions.assertEquals(expectedValue, acc.getBalance());
    }

    @Test
    public void withdrawShouldThrowExceptionWhenAmountGreaterThanBalance() {

        Account acc = new Account(1L, 100.0);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            acc.withdraw(200.0); // tentando sacar mais do que o saldo
        });
    }

    @Test
    public void fullWithdrawShouldClearBalanceAndReturnPreviousValue() {
        // Arrange
        Account acc = new Account(1L, 800.0);

        // Act
        double result = acc.fullWithdraw();

        // Assert
        Assertions.assertEquals(800.0, result);         // retornou o saldo anterior
        Assertions.assertEquals(0.0, acc.getBalance()); // saldo agora é zero
    }
}
