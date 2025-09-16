package com.codedifferently.lesson17.bank;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.Assertions.within;

import org.junit.jupiter.api.Test;

class CurrencyTest {
  private static final double TOLERANCE = 0.0001;

  @Test
  void testConversionToUSD() {
    // Test EUR to USD
    assertThat(Currency.EUR.toUSD(100.0)).isCloseTo(109.0, within(TOLERANCE));

    // Test GBP to USD
    assertThat(Currency.GBP.toUSD(100.0)).isCloseTo(124.0, within(TOLERANCE));

    // Test JPY to USD (testing with larger amount due to small exchange rate)
    assertThat(Currency.JPY.toUSD(10000.0)).isCloseTo(67.0, within(TOLERANCE));

    // Test CAD to USD
    assertThat(Currency.CAD.toUSD(100.0)).isCloseTo(74.0, within(TOLERANCE));

    // USD to USD should be same value
    assertThat(Currency.USD.toUSD(100.0)).isCloseTo(100.0, within(TOLERANCE));
  }

  @Test
  void testConversionFromUSD() {
    // Test USD to EUR
    assertThat(Currency.EUR.fromUSD(109.0)).isCloseTo(100.0, within(TOLERANCE));

    // Test USD to GBP
    assertThat(Currency.GBP.fromUSD(124.0)).isCloseTo(100.0, within(TOLERANCE));

    // Test USD to JPY (testing with larger amount due to small exchange rate)
    assertThat(Currency.JPY.fromUSD(67.0)).isCloseTo(10000.0, within(TOLERANCE));

    // Test USD to CAD
    assertThat(Currency.CAD.fromUSD(74.0)).isCloseTo(100.0, within(TOLERANCE));

    // USD to USD should be same value
    assertThat(Currency.USD.fromUSD(100.0)).isCloseTo(100.0, within(TOLERANCE));
  }

  @Test
  void testRoundTripConversion() {
    double originalAmount = 100.0;

    for (Currency currency : Currency.values()) {
      // Convert to USD and back
      double usdAmount = currency.toUSD(originalAmount);
      double convertedBack = currency.fromUSD(usdAmount);

      // Should get (approximately) the original amount back
      assertThat(convertedBack)
          .as("Round trip conversion for %s", currency)
          .isCloseTo(originalAmount, within(TOLERANCE));
    }
  }

  @Test
  void testNegativeAmounts() {
    // Test negative amount to USD
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> Currency.EUR.toUSD(-100.0))
        .withMessage("Amount cannot be negative");

    // Test negative amount from USD
    assertThatExceptionOfType(IllegalArgumentException.class)
        .isThrownBy(() -> Currency.EUR.fromUSD(-100.0))
        .withMessage("Amount cannot be negative");
  }

  @Test
  void testZeroAmounts() {
    for (Currency currency : Currency.values()) {
      // Zero to USD should be zero
      assertThat(currency.toUSD(0.0)).isZero();

      // Zero USD to any currency should be zero
      assertThat(currency.fromUSD(0.0)).isZero();
    }
  }
}
