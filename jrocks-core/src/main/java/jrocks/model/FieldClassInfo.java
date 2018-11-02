package jrocks.model;

import jrocks.plugin.api.FieldApi;

public interface FieldClassInfo extends FieldApi {

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

  String getter();

  String setter();
}
