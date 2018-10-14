package jrocks.model;

import jrocks.api.FieldClassInfoApi;
import jrocks.template.annotations.BuilderProperty;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.*;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.stream.Stream;

public class FieldClassInfo extends BaseClassInfo implements FieldClassInfoApi {

  private Field field;

  FieldClassInfo(Field field) {
    super(field.getType());
    this.field = field;
  }

  @Override
  public String fieldName() {
    return field.getName();
  }

  @Override
  public String fieldNameCapitalized() {
    return StringUtils.capitalize(field.getName());
  }

  @Override
  public Stream<Annotation> fieldAnnotations() {
    return Stream.of(field.getAnnotations());
  }

  @Override
  public boolean isAnnotatedWith(Class<? extends Annotation>... annotationClasses) {
    return Stream.of(annotationClasses).anyMatch(clazz -> field.getAnnotation(clazz) != null);
  }

  @Override
  public boolean hasNotNull() {
    return field.getAnnotation(NotNull.class) != null;
  }

  @Override
  public boolean hasNotEmpty() {
    return field.getAnnotation(NotEmpty.class) != null;
  }

  @Override
  public boolean hasNotBlank() {
    return field.getAnnotation(NotBlank.class) != null;
  }

  @Override
  public boolean hasDigits() {
    return field.getAnnotation(Digits.class) != null;
  }

  @Override
  public boolean hasEmail() {
    return field.getAnnotation(Email.class) != null;
  }

  @Override
  public boolean hasFuture() {
    return field.getAnnotation(Future.class) != null;
  }

  @Override
  public boolean hasMax() {
    return field.getAnnotation(Max.class) != null;
  }

  @Override
  public boolean hasMin() {
    return field.getAnnotation(Min.class) != null;
  }

  @Override
  public boolean hasNegative() {
    return field.getAnnotation(Negative.class) != null;
  }

  @Override
  public boolean hasNegativeOrZero() {
    return field.getAnnotation(NegativeOrZero.class) != null;
  }

  @Override
  public boolean hasNull() {
    return field.getAnnotation(Null.class) != null;
  }

  @Override
  public boolean hasPast() {
    return field.getAnnotation(Past.class) != null;
  }

  @Override
  public boolean hasPastOrPresent() {
    return field.getAnnotation(PastOrPresent.class) != null;
  }

  @Override
  public boolean hasPattern() {
    return field.getAnnotation(Pattern.class) != null;
  }

  @Override
  public boolean hasPositive() {
    return field.getAnnotation(Positive.class) != null;
  }

  @Override
  public boolean hasPositiveOrZero() {
    return field.getAnnotation(PositiveOrZero.class) != null;
  }

  @Override
  public String maxSize() {
    final Size size = field.getAnnotation(Size.class);
    final DecimalMax decimalMax = field.getAnnotation(DecimalMax.class);
    final Max max = field.getAnnotation(Max.class);
    return size != null ? String.valueOf(size.max())
        : decimalMax != null ? decimalMax.value()
        : max != null ? String.valueOf(max.value()) : null;
  }

  @Override
  public String minSize() {
    final Size size = field.getAnnotation(Size.class);
    final DecimalMin decimalMin = field.getAnnotation(DecimalMin.class);
    final Min min = field.getAnnotation(Min.class);
    return size != null ? String.valueOf(size.min())
        : decimalMin != null ? decimalMin.value()
        : min != null ? String.valueOf(min.value()) : null;
  }

  @Override
  public int digitsInteger() {
    final Digits digits = field.getAnnotation(Digits.class);
    return digits != null ? digits.integer() : 0;
  }

  @Override
  public int digitsFraction() {
    final Digits digits = field.getAnnotation(Digits.class);
    return digits != null ? digits.fraction() : 0;
  }

  @Override
  public boolean isRequired() {
    return isAnnotatedWith(BuilderProperty.class, NotNull.class, NotEmpty.class, NotBlank.class);
  }

  @Override
  public String getter() {
    try {
      return Stream.of(Introspector.getBeanInfo(field.getDeclaringClass()).getMethodDescriptors())
          .filter(m -> {
            final String capitalizeName = StringUtils.capitalize(field.getName());
            return m.getMethod().getName().equals("get" + capitalizeName)
                || m.getMethod().getName().equals("is" + capitalizeName)
                || m.getMethod().getName().equals("has" + capitalizeName);
          })
          .map(FeatureDescriptor::getName)
          .findAny()
          .orElseThrow(() -> new IllegalStateException("field '" + fieldName() + "' need a getter!"));
    } catch (IntrospectionException e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  @Override
  public String setter() {
    try {
      return Stream.of(Introspector.getBeanInfo(field.getDeclaringClass()).getMethodDescriptors())
          .filter(m -> m.getMethod().getName().equals("set" + StringUtils.capitalize(field.getName())))
          .map(FeatureDescriptor::getName)
          .findAny()
          .orElseThrow(() -> new IllegalStateException("field '" + fieldName() + "' need a setter!"));
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
