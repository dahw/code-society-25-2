package com.codedifferently.lesson17.bank;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/** Represents a customer of the bank. */
public class Customer {

  private final UUID id;
  private final String name;
  private final Set<Account> accounts = new HashSet<>();

  /**
   * Creates a new customer.
   *
   * @param id The ID of the customer.
   * @param name The name of the customer.
   */
  public Customer(UUID id, String name) {
    this.id = id;
    this.name = name;
  }

  /**
   * Gets the ID of the customer.
   *
   * @return The ID of the customer.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Gets the name of the customer.
   *
   * @return The name of the customer.
   */
  public String getName() {
    return name;
  }

  /**
   * Adds an account to the customer.
   *
   * @param account The account to add.
   */
  public void addAccount(Account account) {
    accounts.add(account);
  }

  /**
   * Gets the accounts owned by the customer.
   *
   * @return The unique set of accounts owned by the customer.
   */
  public Set<Account> getAccounts() {
    return accounts;
  }

  /**
   * Helper method to get accounts of a specific type.
   *
   * @param <T> The type of account
   * @param accountType The class of the account type
   * @return Set of accounts of the specified type
   */
  @SuppressWarnings("unchecked")
  private <T extends Account> Set<T> getAccountsOfType(Class<T> accountType) {
    return accounts.stream()
        .filter(accountType::isInstance)
        .map(account -> (T) account)
        .collect(java.util.stream.Collectors.toSet());
  }

  /**
   * Gets all checking accounts owned by the customer.
   *
   * @return Set of checking accounts owned by the customer
   */
  public Set<CheckingAccount> getCheckingAccounts() {
    return getAccountsOfType(CheckingAccount.class);
  }

  /**
   * Gets all savings accounts owned by the customer.
   *
   * @return Set of savings accounts owned by the customer
   */
  public Set<SavingsAccount> getSavingsAccounts() {
    return getAccountsOfType(SavingsAccount.class);
  }

  /**
   * Helper method to check if customer has an account of a specific type.
   *
   * @param accountType The class of the account type to check for
   * @return true if the customer has at least one account of the specified type
   */
  private boolean hasAccountOfType(Class<? extends Account> accountType) {
    return accounts.stream().anyMatch(accountType::isInstance);
  }

  /**
   * Checks if the customer has a checking account.
   *
   * @return true if the customer has at least one checking account
   */
  public boolean hasCheckingAccount() {
    return hasAccountOfType(CheckingAccount.class);
  }

  /**
   * Checks if the customer has a savings account.
   *
   * @return true if the customer has at least one savings account
   */
  public boolean hasSavingsAccount() {
    return hasAccountOfType(SavingsAccount.class);
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Customer other) {
      return id.equals(other.id);
    }
    return false;
  }

  @Override
  public String toString() {
    return "Customer{" + "id=" + id + ", name='" + name + '\'' + '}';
  }
}
