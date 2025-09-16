package com.codedifferently.lesson17.bank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.within;

import com.codedifferently.lesson17.bank.exceptions.AccountNotFoundException;
import com.codedifferently.lesson17.bank.exceptions.CheckVoidedException;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BankAtmTest {

  private BankAtm classUnderTest;
  private CheckingAccount checkingAccount1;
  private CheckingAccount checkingAccount2;
  private SavingsAccount savingsAccount;
  private Customer customer1;
  private Customer customer2;

  @BeforeEach
  void setUp() {
    classUnderTest = new BankAtm();
    customer1 = new Customer(UUID.randomUUID(), "John Doe");
    customer2 = new Customer(UUID.randomUUID(), "Jane Smith");

    checkingAccount1 = new CheckingAccount("123456789", Set.of(customer1), 100.0);
    checkingAccount2 = new CheckingAccount("987654321", Set.of(customer1, customer2), 200.0);
    savingsAccount = new SavingsAccount("555555555", Set.of(customer1), 300.0);

    customer1.addAccount(checkingAccount1);
    customer1.addAccount(checkingAccount2);
    customer1.addAccount(savingsAccount);
    customer2.addAccount(checkingAccount2);

    classUnderTest.addAccount(checkingAccount1);
    classUnderTest.addAccount(checkingAccount2);
    classUnderTest.addAccount(savingsAccount);
  }

  @Test
  void testAddAccount() {
    // Arrange
    Customer customer3 = new Customer(UUID.randomUUID(), "Alice Johnson");
    CheckingAccount newCheckingAccount = new CheckingAccount("444444444", Set.of(customer3), 300.0);
    customer3.addAccount(newCheckingAccount);

    // Act
    classUnderTest.addAccount(newCheckingAccount);

    // Assert
    Set<Account> accounts = classUnderTest.findAccountsByCustomerId(customer3.getId());
    assertThat(accounts).containsOnly(newCheckingAccount);
  }

  @Test
  void testFindAccountsByCustomerId() {
    // Act
    Set<Account> accounts = classUnderTest.findAccountsByCustomerId(customer1.getId());

    // Assert
    assertThat(accounts).containsOnly(checkingAccount1, checkingAccount2, savingsAccount);
  }

  @Test
  void testDepositFunds() {
    // Act
    classUnderTest.depositFunds(checkingAccount1.getAccountNumber(), 50.0);

    // Assert
    assertThat(checkingAccount1.getBalance()).isEqualTo(150.0);
  }

  @Test
  void testDepositFunds_WithDifferentCurrencies() {
    // Act - Deposit 100 EUR (= 109 USD)
    classUnderTest.depositFunds(checkingAccount1.getAccountNumber(), 100.0, Currency.EUR);

    // Assert
    assertThat(checkingAccount1.getBalance())
        .isCloseTo(209.0, within(0.0001)); // 100 initial + 109 USD

    // Act - Deposit 100 GBP (= 124 USD)
    classUnderTest.depositFunds(checkingAccount1.getAccountNumber(), 100.0, Currency.GBP);

    // Assert
    assertThat(checkingAccount1.getBalance()).isCloseTo(333.0, within(0.0001)); // 209 + 124 USD
  }

  @Test
  void testDepositFunds_WithZeroAmountInForeignCurrency() {
    // Act
    classUnderTest.depositFunds(checkingAccount1.getAccountNumber(), 0.0, Currency.EUR);

    // Assert - Balance should remain unchanged
    assertThat(checkingAccount1.getBalance()).isEqualTo(100.0);
  }

  @Test
  void testDepositFunds_WithNegativeAmountInForeignCurrency() {
    // Act & Assert
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(
            () ->
                classUnderTest.depositFunds(
                    checkingAccount1.getAccountNumber(), -50.0, Currency.EUR))
        .withMessage("Deposit amount must be positive");
  }

  @Test
  void testDepositFunds_Check() {
    // Arrange
    Check check = new Check("987654321", 100.0, checkingAccount1);

    // Act
    classUnderTest.depositFunds(checkingAccount2.getAccountNumber(), check);

    // Assert
    assertThat(checkingAccount1.getBalance()).isEqualTo(0);
    assertThat(checkingAccount2.getBalance()).isEqualTo(300.0);
  }

  @Test
  void testDepositFunds_CheckToSavings() {
    // Arrange
    Check check = new Check("987654321", 100.0, checkingAccount1);

    // Act & Assert
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> classUnderTest.depositFunds(savingsAccount.getAccountNumber(), check))
        .withMessage("Cannot deposit checks into a savings account");
  }

  @Test
  void testDepositFunds_DoesntDepositCheckTwice() {
    Check check = new Check("987654321", 100.0, checkingAccount1);

    classUnderTest.depositFunds(checkingAccount2.getAccountNumber(), check);

    assertThatExceptionOfType(CheckVoidedException.class)
        .isThrownBy(() -> classUnderTest.depositFunds(checkingAccount2.getAccountNumber(), check))
        .withMessage("Check is voided");
  }

  @Test
  void testWithdrawFunds() {
    // Act
    classUnderTest.withdrawFunds(checkingAccount2.getAccountNumber(), 50.0);

    // Assert
    assertThat(checkingAccount2.getBalance()).isEqualTo(150.0);
  }

  @Test
  void testWithdrawFunds_AccountNotFound() {
    String nonExistingAccountNumber = "999999999";

    // Act & Assert
    assertThatExceptionOfType(AccountNotFoundException.class)
        .isThrownBy(() -> classUnderTest.withdrawFunds(nonExistingAccountNumber, 50.0))
        .withMessage("Account not found");
  }
}
