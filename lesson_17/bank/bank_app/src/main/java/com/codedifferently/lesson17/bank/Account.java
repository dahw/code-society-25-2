package com.codedifferently.lesson17.bank;

import com.codedifferently.lesson17.bank.exceptions.InsufficientFundsException;
import java.util.Set;

/**
 * Common interface for all bank accounts. Defines the standard operations that all account types
 * must support, including deposits, withdrawals, and account management. Specific account types may
 * have additional restrictions (e.g., SavingsAccount does not accept checks).
 */
public interface Account {
  /**
   * Gets the unique account number for this account.
   *
   * @return The account number
   */
  String getAccountNumber();

  /**
   * Gets the set of customers who own this account.
   *
   * @return Set of account owners
   */
  Set<Customer> getOwners();

  /**
   * Gets the current balance of the account.
   *
   * @return The current balance
   */
  double getBalance();

  /**
   * Helper method to validate deposit amount.
   *
   * @param amount The amount to validate
   * @throws IllegalArgumentException if amount is not positive
   */
  private static void validateDepositAmount(double amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Amount must be positive");
    }
  }

  /**
   * Helper method to validate withdrawal amount.
   *
   * @param amount The amount to validate
   * @throws IllegalArgumentException if amount is not positive
   */
  private static void validateWithdrawalAmount(double amount) {
    if (amount <= 0) {
      throw new IllegalArgumentException("Withdrawal amount must be positive");
    }
  }

  /**
   * Deposits funds into the account.
   *
   * @param amount The amount to deposit
   * @throws IllegalStateException if the account is closed
   */
  default void deposit(double amount) throws IllegalStateException {
    validateDepositAmount(amount);
    internalDeposit(amount);
  }

  /** Internal method to handle the actual deposit. */
  void internalDeposit(double amount) throws IllegalStateException;

  /**
   * Withdraws funds from the account.
   *
   * @param amount The amount to withdraw
   * @throws InsufficientFundsException if there are insufficient funds
   * @throws IllegalStateException if the account is closed
   */
  default void withdraw(double amount) throws InsufficientFundsException {
    validateWithdrawalAmount(amount);
    internalWithdraw(amount);
  }

  /** Internal method to handle the actual withdrawal. */
  void internalWithdraw(double amount) throws InsufficientFundsException;

  /**
   * Closes the account. An account cannot be closed if it has a positive balance.
   *
   * @throws IllegalStateException if the account has a positive balance
   */
  void closeAccount() throws IllegalStateException;

  /**
   * Checks if the account is closed.
   *
   * @return true if the account is closed, false otherwise
   */
  boolean isClosed();
}
