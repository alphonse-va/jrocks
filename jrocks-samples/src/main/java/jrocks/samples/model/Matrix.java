package jrocks.samples.model;

import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.time.LocalDate;

public class Matrix {

  @NotEmpty
  @Pattern(regexp = "[a-z]")
  @Size(min = 10, max = 20)
  private String username;

  @NotEmpty
  @NotBlank
  @NotNull
  private String password;

  @PositiveOrZero
  @Positive
  @NegativeOrZero
  @Negative
  @Null
  @Min(10)
  @Max(100)
  @Digits(integer = 10, fraction = 2)
  private BigDecimal digit;

  @NotNull
  @DecimalMin("10.1")
  @DecimalMax("10.1")
  private BigDecimal decimal;

  @NotNull
  @FutureOrPresent
  @Future
  @Past
  @PastOrPresent
  private LocalDate date;

  @Email
  private String email;

  public String getUsername() {
    return username;
  }

  public Matrix setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getPassword() {
    return password;
  }

  public Matrix setPassword(String password) {
    this.password = password;
    return this;
  }

  public BigDecimal getDigit() {
    return digit;
  }

  public Matrix setDigit(BigDecimal digit) {
    this.digit = digit;
    return this;
  }

  public BigDecimal getDecimal() {
    return decimal;
  }

  public Matrix setDecimal(BigDecimal decimal) {
    this.decimal = decimal;
    return this;
  }

  public LocalDate getDate() {
    return date;
  }

  public Matrix setDate(LocalDate date) {
    this.date = date;
    return this;
  }

  public String getEmail() {
    return email;
  }

  public Matrix setEmail(String email) {
    this.email = email;
    return this;
  }


  @Override
  public String toString() {
    return new ToStringBuilder(this)
        .append("username", username)
        .append("password", password)
        .append("digit", digit)
        .append("decimal", decimal)
        .append("date", date)
        .append("email", email)
        .toString();
  }
}
