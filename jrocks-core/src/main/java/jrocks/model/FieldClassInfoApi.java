package jrocks.model;

import io.github.classgraph.AnnotationInfo;

import java.util.stream.Stream;

public interface FieldClassInfoApi extends ClassInfoApi {

  String fieldName();

  String fieldNameCapitalized();

  Stream<AnnotationInfo> fieldAnnotations();

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
