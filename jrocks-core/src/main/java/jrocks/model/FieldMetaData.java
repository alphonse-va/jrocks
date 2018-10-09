package jrocks.model;

import jrocks.template.annotations.BuilderProperty;

import javax.validation.constraints.*;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.stream.Stream;

public class FieldMetaData<T> extends AbstractMetaData<T> {

  private Field field;

  @SuppressWarnings("unchecked")
  public FieldMetaData(Field field) {
    super((Class<T>) field.getType());
    this.field = field;
  }

  public String fieldName() {
    return field.getName();
  }

  public String fieldNameCapitalized() {
    return INFLECTOR.capitalize(field.getName());
  }

  public Stream<Annotation> fieldAnnotations() {
    return Stream.of(field.getAnnotations());
  }

  public boolean isAnnotatedWith(Class<? extends Annotation>... annotationClass) {
    return Stream.of(annotationClass).anyMatch(clazz -> field.getAnnotation(clazz) != null);
  }

  public boolean hasNotNull() {
    return field.getAnnotation(NotNull.class) != null;
  }

  public boolean hasNotEmpty() {
    return field.getAnnotation(NotEmpty.class) != null;
  }

  public boolean hasNotBlank() {
    return field.getAnnotation(NotBlank.class) != null;
  }

  public boolean hasDigits() {
    return field.getAnnotation(Digits.class) != null;
  }

  public boolean hasEmail() {
    return field.getAnnotation(Email.class) != null;
  }

  public boolean hasFuture() {
    return field.getAnnotation(Future.class) != null;
  }

  public boolean hasMax() {
    return field.getAnnotation(Max.class) != null;
  }

  public boolean hasMin() {
    return field.getAnnotation(Min.class) != null;
  }

  public boolean hasNegative() {
    return field.getAnnotation(Negative.class) != null;
  }

  public boolean hasNegativeOrZero() {
    return field.getAnnotation(NegativeOrZero.class) != null;
  }

  public boolean hasNull() {
    return field.getAnnotation(Null.class) != null;
  }

  public boolean hasPast() {
    return field.getAnnotation(Past.class) != null;
  }

  public boolean hasPastOrPresent() {
    return field.getAnnotation(PastOrPresent.class) != null;
  }

  public boolean hasPattern() {
    return field.getAnnotation(Pattern.class) != null;
  }

  public boolean hasPositive() {
    return field.getAnnotation(Positive.class) != null;
  }

  public boolean hasPositiveOrZero() {
    return field.getAnnotation(PositiveOrZero.class) != null;
  }

  public String maxSize() {
    final Size size = field.getAnnotation(Size.class);
    final DecimalMax decimalMax = field.getAnnotation(DecimalMax.class);
    final Max max = field.getAnnotation(Max.class);
    return size != null ? String.valueOf(size.max())
        : decimalMax != null ? decimalMax.value()
        : max != null ? String.valueOf(max.value()) : null;
  }

  public String minSize() {
    final Size size = field.getAnnotation(Size.class);
    final DecimalMin decimalMin = field.getAnnotation(DecimalMin.class);
    final Min min = field.getAnnotation(Min.class);
    return size != null ? String.valueOf(size.min())
        : decimalMin != null ? decimalMin.value()
        : min != null ? String.valueOf(min.value()) : null;
  }

  public int digitsInteger() {
    final Digits digits = field.getAnnotation(Digits.class);
    return digits != null ? digits.integer() : 0;
  }

  public int digitsFraction() {
    final Digits digits = field.getAnnotation(Digits.class);
    return digits != null ? digits.fraction() : 0;
  }

  public boolean isRequired() {
    return isAnnotatedWith(BuilderProperty.class, NotNull.class, NotEmpty.class, NotBlank.class);
  }

  public String getter() {
    try {
      return Stream.of(Introspector.getBeanInfo(field.getDeclaringClass()).getMethodDescriptors())
          .filter(this::isGetter)
          .map(FeatureDescriptor::getName)
          .findAny()
          .orElseThrow(() -> new IllegalStateException("field '" + fieldName() + "' required a getter!"));
    } catch (IntrospectionException e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  public String setter() {
    try {
      return Stream.of(Introspector.getBeanInfo(field.getDeclaringClass()).getMethodDescriptors())
          .filter(this::isSetter)
          .map(FeatureDescriptor::getName)
          .findAny()
          .orElseThrow(() -> new IllegalStateException("field '" + fieldName() + "' required a setter!"));
    } catch (IntrospectionException e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  private boolean isSetter(MethodDescriptor m) {
    final Class<?>[] parameterTypes = m.getMethod().getParameterTypes();
    if (parameterTypes.length > 1) {
      return false;
    }
    final boolean hasSetterParamType = Stream.of(parameterTypes).anyMatch(t -> t.equals(field.getType()));
    return hasSetterParamType && !m.isHidden() && m.getName().equals("set" + fieldNameCapitalized());
  }

  private boolean isGetter(MethodDescriptor m) {
    final boolean hasReturnType = m.getMethod().getReturnType().equals(field.getType());
    return hasReturnType && !m.isHidden()
        && (m.getName().equals("get" + fieldNameCapitalized())
        || m.getName().equals("is" + fieldNameCapitalized())
        || m.getName().equals("has" + fieldNameCapitalized()));
  }
}
