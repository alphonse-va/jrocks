package jrocks;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Builder for the Matrix bean.
 */
public class MatrixBuilder {

  private Matrix matrix = new Matrix();

  public Matrix build() {
    Objects.requireNonNull(matrix.getUsername(), "Username is required!");
    Objects.requireNonNull(matrix.getPassword(), "Password is required!");
    Objects.requireNonNull(matrix.getDecimal(), "Decimal is required!");
    Objects.requireNonNull(matrix.getDate(), "Date is required!");
    return matrix;
  }

  public MatrixBuilder setUsername(String username) {
    matrix.setUsername(username);
    return this;
  }

  public MatrixBuilder setPassword(String password) {
    matrix.setPassword(password);
    return this;
  }

  public MatrixBuilder setDecimal(BigDecimal decimal) {
    matrix.setDecimal(decimal);
    return this;
  }

  public MatrixBuilder setDate(LocalDate date) {
    matrix.setDate(date);
    return this;
  }

}