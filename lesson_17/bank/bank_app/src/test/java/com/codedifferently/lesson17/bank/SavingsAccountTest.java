package com.codedifferently.lesson17.bank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

import com.codedifferently.lesson17.bank.exceptions.InsufficientFundsException;
import java.util.Set;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SavingsAccountTest {
  private SavingsAccount classUnderTest;
  private Customer owner;

  @BeforeEach
  void setUp() {
    owner = new Customer(UUID.randomUUID(), "John Doe");
    classUnderTest = new SavingsAccount("123456789", Set.of(owner), 100.0);
    owner.addAccount(classUnderTest);
  }

  @Test
  void testDeposit_Success() {
    // Act
    classUnderTest.deposit(50.0);

    // Assert
    assertThat(classUnderTest.getBalance()).isEqualTo(150.0);
  }

  @Test
  void testDeposit_NegativeAmount() {
    // Act & Assert
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> classUnderTest.deposit(-50.0))
        .withMessage("Amount must be positive");
  }

  @Test
  void testDeposit_ExceedsLimit() {
    // Act & Assert
    assertThatExceptionOfType(UnsupportedOperationException.class)
        .isThrownBy(() -> classUnderTest.deposit(10000.0))
        .withMessage("Savings accounts do not accept deposits over $10,000");
  }

  @Test
  void testWithdraw_Success() {
    // Act
    classUnderTest.withdraw(50.0);

    // Assert
    assertThat(classUnderTest.getBalance()).isEqualTo(50.0);
  }

  @Test
  void testWithdraw_NegativeAmount() {
    // Act & Assert
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> classUnderTest.withdraw(-50.0))
        .withMessage("Amount must be positive");
  }

  @Test
  void testWithdraw_InsufficientFunds() {
    // Act & Assert
    assertThatExceptionOfType(InsufficientFundsException.class)
        .isThrownBy(() -> classUnderTest.withdraw(150.0))
        .withMessage("Account does not have enough funds for withdrawal");
  }

  @Test
  void testCloseAccount_WithZeroBalance() {
    // Arrange
    classUnderTest.withdraw(100.0);

    // Act
    classUnderTest.closeAccount();

    // Assert
    assertThat(classUnderTest.isClosed()).isTrue();
  }

  @Test
  void testCloseAccount_WithPositiveBalance() {
    // Act & Assert
    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> classUnderTest.closeAccount())
        .withMessage("Cannot close account with a positive balance");
  }

  @Test
  void testDepositToClosedAccount() {
    // Arrange
    classUnderTest.withdraw(100.0);
    classUnderTest.closeAccount();

    // Act & Assert
    assertThatExceptionOfType(IllegalStateException.class)
        .isThrownBy(() -> classUnderTest.deposit(50.0))
        .withMessage("Cannot deposit to a closed account");
  }
}
