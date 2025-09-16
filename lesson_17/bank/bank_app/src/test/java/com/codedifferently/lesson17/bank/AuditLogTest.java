package com.codedifferently.lesson17.bank;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AuditLogTest {
  private static final String TEST_ACCOUNT_NUMBER = "123456789";

  @BeforeEach
  void setUp() {
    // Clear all transactions between tests
    AuditLog.clearTransactions();
  }

  @Test
  void testLogTransaction_Deposit() {
    // Arrange
    double amount = 50.0;

    // Act
    AuditLog.logTransaction(
        TEST_ACCOUNT_NUMBER,
        AuditLog.TransactionType.DEPOSIT,
        amount,
        AuditLog.TransactionInstrument.CASH);

    // Assert
    List<AuditLog.Transaction> transactions =
        AuditLog.getTransactionsForAccount(TEST_ACCOUNT_NUMBER);
    assertThat(transactions).hasSize(1);

    AuditLog.Transaction transaction = transactions.get(0);
    assertThat(transaction.accountNumber()).isEqualTo(TEST_ACCOUNT_NUMBER);
    assertThat(transaction.type()).isEqualTo(AuditLog.TransactionType.DEPOSIT);
    assertThat(transaction.instrument()).isEqualTo(AuditLog.TransactionInstrument.CASH);
    assertThat(transaction.amount()).isEqualTo(50.0);
  }

  @Test
  void testLogTransaction_Withdrawal() {
    // Arrange
    double amount = 50.0;

    // Act
    AuditLog.logTransaction(
        TEST_ACCOUNT_NUMBER,
        AuditLog.TransactionType.WITHDRAWAL,
        amount,
        AuditLog.TransactionInstrument.CASH);

    // Assert
    List<AuditLog.Transaction> transactions =
        AuditLog.getTransactionsForAccount(TEST_ACCOUNT_NUMBER);
    assertThat(transactions).hasSize(1);

    AuditLog.Transaction transaction = transactions.get(0);
    assertThat(transaction.accountNumber()).isEqualTo(TEST_ACCOUNT_NUMBER);
    assertThat(transaction.type()).isEqualTo(AuditLog.TransactionType.WITHDRAWAL);
    assertThat(transaction.instrument()).isEqualTo(AuditLog.TransactionInstrument.CASH);
    assertThat(transaction.amount()).isEqualTo(50.0);
  }

  @Test
  void testLogTransaction_MultipleTransactions() {
    // Act
    AuditLog.logTransaction(
        TEST_ACCOUNT_NUMBER,
        AuditLog.TransactionType.DEPOSIT,
        50.0,
        AuditLog.TransactionInstrument.CASH);

    AuditLog.logTransaction(
        TEST_ACCOUNT_NUMBER,
        AuditLog.TransactionType.WITHDRAWAL,
        25.0,
        AuditLog.TransactionInstrument.CHECK);

    // Assert
    List<AuditLog.Transaction> transactions =
        AuditLog.getTransactionsForAccount(TEST_ACCOUNT_NUMBER);
    assertThat(transactions).hasSize(2);
    assertThat(transactions)
        .extracting(AuditLog.Transaction::type)
        .containsExactly(AuditLog.TransactionType.DEPOSIT, AuditLog.TransactionType.WITHDRAWAL);

    assertThat(transactions)
        .extracting(AuditLog.Transaction::instrument)
        .containsExactly(AuditLog.TransactionInstrument.CASH, AuditLog.TransactionInstrument.CHECK);
  }

  @Test
  void testGetTransactions_NoTransactions() {
    // Act
    List<AuditLog.Transaction> transactions = AuditLog.getTransactionsForAccount("987654321");

    // Assert
    assertThat(transactions).isEmpty();
  }

  @Test
  void testGetTransactions_OrderedByTimestamp() {
    // Act
    for (int i = 0; i < 5; i++) {
      AuditLog.logTransaction(
          TEST_ACCOUNT_NUMBER,
          AuditLog.TransactionType.DEPOSIT,
          10.0,
          AuditLog.TransactionInstrument.CASH);
    }

    // Assert
    List<AuditLog.Transaction> transactions =
        AuditLog.getTransactionsForAccount(TEST_ACCOUNT_NUMBER);
    assertThat(transactions).hasSize(5);
    assertThat(transactions).extracting(AuditLog.Transaction::timestamp).isSorted();
  }
}
