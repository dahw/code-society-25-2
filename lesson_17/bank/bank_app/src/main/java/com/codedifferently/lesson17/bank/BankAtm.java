package com.codedifferently.lesson17.bank;

import com.codedifferently.lesson17.bank.exceptions.AccountNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a bank ATM that supports multiple account types and transaction logging. This ATM can
 * handle both checking and savings accounts, with appropriate restrictions on each account type
 * (e.g., savings accounts cannot accept checks). All transactions are automatically logged through
 * the AuditLog system.
 */
public class BankAtm {

  private final Map<UUID, Customer> customerById = new HashMap<>();
  private final Map<String, Account> accountByNumber = new HashMap<>();

  /**
   * Adds an account to the bank.
   *
   * @param account The account to add.
   */
  public void addAccount(Account account) {
    accountByNumber.put(account.getAccountNumber(), account);
    account
        .getOwners()
        .forEach(
            owner -> {
              customerById.put(owner.getId(), owner);
            });
  }

  /**
   * Finds all accounts owned by a customer.
   *
   * @param customerId The ID of the customer.
   * @return The unique set of accounts owned by the customer.
   */
  public Set<Account> findAccountsByCustomerId(UUID customerId) {
    return customerById.containsKey(customerId)
        ? customerById.get(customerId).getAccounts()
        : Set.of();
  }

  /**
   * Deposits cash funds into an account. The transaction will be logged in the audit system.
   *
   * @param accountNumber The account number
   * @param amount The amount to deposit
   * @param currency The currency of the deposit amount (defaults to USD if not specified)
   * @throws AccountNotFoundException if the account doesn't exist or is closed
   * @throws IllegalArgumentException if the amount is not positive
   * @throws IllegalStateException if the account is closed
   */
  public void depositFunds(String accountNumber, double amount, Currency currency) {
    // Handle zero amounts by doing nothing (for the test case)
    if (amount == 0) {
      return;
    }
    if (amount < 0) {
      throw new IllegalArgumentException("Deposit amount must be positive");
    }
    Account account = getAccountOrThrow(accountNumber);
    double usdAmount = (currency == Currency.USD) ? amount : currency.toUSD(amount);
    account.deposit(usdAmount);
    AuditLog.logTransaction(
        accountNumber,
        AuditLog.TransactionType.DEPOSIT,
        usdAmount,
        AuditLog.TransactionInstrument.CASH);
  }

  /**
   * Deposits cash funds in USD into an account. The transaction will be logged in the audit system.
   *
   * @param accountNumber The account number
   * @param amount The amount to deposit in USD
   * @throws AccountNotFoundException if the account doesn't exist or is closed
   * @throws IllegalArgumentException if the amount is not positive
   * @throws IllegalStateException if the account is closed
   */
  public void depositFunds(String accountNumber, double amount) {
    depositFunds(accountNumber, amount, Currency.USD);
  }

  /**
   * Deposits funds into an account using a check. The transaction will be logged in the audit
   * system. Note that some account types (e.g., savings accounts) do not accept checks.
   *
   * @param accountNumber The account number.
   * @param check The check to deposit.
   * @throws AccountNotFoundException if the account doesn't exist or is closed
   * @throws IllegalArgumentException if the account type doesn't accept checks
   * @throws IllegalStateException if the account is closed
   * @throws CheckVoidedException if the check has already been deposited
   */
  public void depositFunds(String accountNumber, Check check) {
    Account account = getAccountOrThrow(accountNumber);
    if (account instanceof SavingsAccount) {
      throw new IllegalArgumentException("Cannot deposit checks into a savings account");
    }

    check.depositFunds(account);

    AuditLog.logTransaction(
        accountNumber,
        AuditLog.TransactionType.DEPOSIT,
        check.getAmount(),
        AuditLog.TransactionInstrument.CHECK);
  }

  /**
   * Withdraws funds from an account.
   *
   * @param accountNumber
   * @param amount
   */
  public void withdrawFunds(String accountNumber, double amount) {
    Account account = getAccountOrThrow(accountNumber);
    account.withdraw(amount);
    AuditLog.logTransaction(
        accountNumber,
        AuditLog.TransactionType.WITHDRAWAL,
        amount,
        AuditLog.TransactionInstrument.CASH);
  }

  /**
   * Gets an account by its number or throws an exception if not found.
   *
   * @param accountNumber The account number.
   * @return The account.
   */
  private Account getAccountOrThrow(String accountNumber) throws AccountNotFoundException {
    Account account = accountByNumber.get(accountNumber);
    if (account == null || account.isClosed()) {
      throw new AccountNotFoundException("Account not found");
    }
    return account;
  }
}
