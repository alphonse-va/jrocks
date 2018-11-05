package jrocks.plugin.api;

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
}
