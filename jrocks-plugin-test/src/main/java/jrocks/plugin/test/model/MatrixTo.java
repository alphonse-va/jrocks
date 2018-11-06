package jrocks.plugin.test.model;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * To for the MatrixTo bean.
 */
public class MatrixTo {

  private String username;
  private BigDecimal digit;
  private BigDecimal decimal;
  private LocalDate date;
  private String email;

  public static MatrixTo from(Matrix from) {
    return new MatrixTo()
        .setUsername(from.getUsername())
        .setDigit(from.getDigit())
        .setDecimal(from.getDecimal())
        .setDate(from.getDate())
        .setEmail(from.getEmail());
  }

  private MatrixTo setUsername(String username) {
    this.username = username;;
    return this;
  }

  public String getUsername() {
    return username;
  }

  private MatrixTo setDigit(BigDecimal digit) {
    this.digit = digit;;
    return this;
  }

  public BigDecimal getDigit() {
    return digit;
  }

  private MatrixTo setDecimal(BigDecimal decimal) {
    this.decimal = decimal;;
    return this;
  }

  public BigDecimal getDecimal() {
    return decimal;
  }

  private MatrixTo setDate(LocalDate date) {
    this.date = date;;
    return this;
  }

  public LocalDate getDate() {
    return date;
  }

  private MatrixTo setEmail(String email) {
    this.email = email;;
    return this;
  }

  public String getEmail() {
    return email;
  }
}