package jrocks.plugin.api;

import java.math.BigDecimal;
import java.util.Optional;

public interface FieldApi extends ClassApi {

  String fieldName();

  String fieldNameCapitalized();

  boolean isAnnotatedWith(String... annotationClasses);

  boolean hasNotNull();

  boolean hasNotEmpty();

  boolean hasNotBlank();

  boolean hasDigits();

  boolean hasEmail();

  boolean hasFuture();

  boolean hasMax();

  boolean hasMin();

  boolean hasNegative();

  boolean hasNegativeOrZero();

  boolean hasNull();

  boolean hasPast();

  boolean hasPastOrPresent();

  boolean hasPattern();

  boolean hasPositive();

  boolean hasPositiveOrZero();

  String maxSize();

  String minSize();

  Integer digitsInteger();

  Integer digitsFraction();

  boolean isRequired();

  Optional<String> getter();

  Optional<String> setter();

  default boolean isString() {
    return name().equals(String.class.getCanonicalName());
  }

  default boolean isNumber() {
    return name().equals(Integer.class.getCanonicalName())
        || name().equals(BigDecimal.class.getCanonicalName())
        || name().equals(Double.class.getCanonicalName())
        || name().equals(Long.class.getCanonicalName())
        || name().equals("int")
        || name().equals("long")
        || name().equals("double");
  }

  default String placeholder() {
    return fieldNameCapitalized().replaceAll("(.)(\\p{Upper})", "$1 $2");
  }
}
