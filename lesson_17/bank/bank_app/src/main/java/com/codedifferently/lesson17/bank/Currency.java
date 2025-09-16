package com.codedifferently.lesson17.bank;

/**
 * Represents supported currencies in the banking system and handles currency conversion operations.
 * Each currency maintains its current exchange rate relative to USD.
 */
public enum Currency {
  USD(1.0), // Base currency
  EUR(1.09), // Euro
  GBP(1.24), // British Pound
  JPY(0.0067), // Japanese Yen
  CAD(0.74); // Canadian Dollar

  private final double usdExchangeRate;

  Currency(double usdExchangeRate) {
    this.usdExchangeRate = usdExchangeRate;
  }

  /**
   * Gets the current exchange rate for converting to USD.
   *
   * @return The exchange rate
   */
  public double getUsdExchangeRate() {
    return usdExchangeRate;
  }

  /**
   * Converts an amount from this currency to USD.
   *
   * @param amount The amount to convert
   * @return The amount in USD
   * @throws IllegalArgumentException if amount is negative
   */
  public double toUSD(double amount) {
    if (amount < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }
    return amount * usdExchangeRate;
  }

  /**
   * Converts an amount from USD to this currency.
   *
   * @param usdAmount The USD amount to convert
   * @return The amount in this currency
   * @throws IllegalArgumentException if amount is negative
   */
  public double fromUSD(double usdAmount) {
    if (usdAmount < 0) {
      throw new IllegalArgumentException("Amount cannot be negative");
    }
    return usdAmount / usdExchangeRate;
  }
}
