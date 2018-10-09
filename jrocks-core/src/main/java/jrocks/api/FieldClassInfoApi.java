package jrocks.api;

import java.lang.annotation.Annotation;
import java.util.stream.Stream;

public interface FieldClassInfoApi extends ClassInfoApi {

  String fieldName();

  String fieldNameCapitalized();

  Stream<Annotation> fieldAnnotations();

  boolean isAnnotatedWith(Class<? extends Annotation>... annotationClass);

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

  int digitsInteger();

  int digitsFraction();

  boolean isRequired();

  String getter();

  String setter();
}
