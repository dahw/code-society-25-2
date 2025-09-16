package com.codedifferently.lesson17.bank;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/** Maintains a record of all financial transactions in the bank. */
public class AuditLog {
  /** Types of banking transactions. */
  public enum TransactionType {
    DEPOSIT,
    WITHDRAWAL
  }

  /** Types of financial instruments used in transactions. */
  public enum TransactionInstrument {
    CASH,
    CHECK,
    MONEY_ORDER
  }

  private static final List<Transaction> transactions = new ArrayList<>();

  /** Clear all transactions - used for testing. */
  public static void clearTransactions() {
    transactions.clear();
  }

  /**
   * Records a transaction in the audit log.
   *
   * @param accountNumber The account number involved in the transaction
   * @param transactionType The type of transaction (DEPOSIT, WITHDRAWAL)
   * @param amount The amount of money involved
   * @param instrument The type of instrument used (CASH, CHECK, MONEY_ORDER)
   */
  public static void logTransaction(
      String accountNumber,
      TransactionType transactionType,
      double amount,
      TransactionInstrument instrument) {
    Transaction transaction =
        new Transaction(accountNumber, transactionType, amount, instrument, LocalDateTime.now());
    transactions.add(transaction);
  }

  /**
   * Gets all transactions for a specific account.
   *
   * @param accountNumber The account number to get transactions for
   * @return List of transactions for the account
   */
  public static List<Transaction> getTransactionsForAccount(String accountNumber) {
    return transactions.stream()
        .filter(t -> t.accountNumber().equals(accountNumber))
        .collect(java.util.stream.Collectors.toCollection(ArrayList::new));
  }

  /**
   * Represents a single transaction in the audit log. Uses transaction types defined in the Account
   * interface.
   */
  public record Transaction(
      String accountNumber,
      TransactionType type,
      double amount,
      TransactionInstrument instrument,
      LocalDateTime timestamp) {}
}
