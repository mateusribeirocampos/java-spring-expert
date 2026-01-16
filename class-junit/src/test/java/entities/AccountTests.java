package entities;

import entities.factories.AccountFactory;
import org.example.entities.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class AccountTests {

    @Test
    public void depositShouldIncreaseBalanceWhenPositiveAmount() {
        // Arrange
        double amount = 200.0;
        double expectedValue = 196.0;
        Account acc = AccountFactory.createEmptyAccount();

        // Act
        acc.deposit(amount);

        // Assert
        Assertions.assertEquals(expectedValue, acc.getBalance());
    }

    @Test
    public void withdrawShouldDecreaseBalanceWhenPositiveAmount() {
        // Arrange
        double expectedValue = 100.0;
        Account acc = AccountFactory.createAccountWithBalance(200.0);

        // Act
        acc.withdraw(100.0);

        // Assert
        Assertions.assertEquals(expectedValue, acc.getBalance());
    }

    @Test
    public void withdrawShouldThrowExceptionWhenAmountGreaterThanBalance() {
        // Arrange
        Account acc = AccountFactory.createAccountWithBalance(100.0);

        // Act & Assert
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            acc.withdraw(200.0); // tentando sacar mais do que o saldo
        });
    }

    @Test
    public void fullWithdrawShouldClearBalanceAndReturnPreviousValue() {
        // Arrange
        double expectedValue = 0.0;
        Account acc = AccountFactory.createAccountWithBalance(800.0);

        // Act
        double result = acc.fullWithdraw();

        // Assert
        Assertions.assertEquals(800.0, result);         // retornou o saldo anterior
        Assertions.assertEquals(0.0, acc.getBalance()); // saldo agora é zero
        Assertions.assertTrue(expectedValue == acc.getBalance());
    }

    @Test
    public void depositShouldDoNothingWhenNegativeAmount() {
        // Arrange
        double expectedValue = 100.0;
        Account acc = AccountFactory.createDefaultAccount();
        double amount = -200.0;

        // Act
        acc.deposit(amount);

        // Assert
        Assertions.assertEquals(expectedValue, acc.getBalance());
    }

    @Test
    public void withdrawShouldDecreaseBalanceWhenSufficientBalance() {
        Account acc = AccountFactory.createAccount(1L, 800.0);

        acc.withdraw(500.0);

        Assertions.assertEquals(300.0, acc.getBalance());
    }

}
