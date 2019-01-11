package jrocks.model;

import jrocks.plugin.api.FieldApi;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.*;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

public class ReflectiveFieldInfo extends ReflectiveClassInfo implements FieldApi {

  private Field field;

  ReflectiveFieldInfo(Field field) {
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
  public boolean isAnnotatedWith(String... annotationClasses) {
    return Stream.of(field.getAnnotations())
        .map(a -> a.annotationType().getCanonicalName())
        .anyMatch(name -> asList(annotationClasses).contains(name));
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
    Size size = field.getAnnotation(Size.class);
    DecimalMax decimalMax = field.getAnnotation(DecimalMax.class);
    Max max = field.getAnnotation(Max.class);
    return size != null ? String.valueOf(size.max())
        : decimalMax != null ? decimalMax.value()
        : max != null ? String.valueOf(max.value()) : null;
  }

  @Override
  public String minSize() {
    Size size = field.getAnnotation(Size.class);
    DecimalMin decimalMin = field.getAnnotation(DecimalMin.class);
    Min min = field.getAnnotation(Min.class);
    return size != null ? String.valueOf(size.min())
        : decimalMin != null ? decimalMin.value()
        : min != null ? String.valueOf(min.value()) : null;
  }

  @Override
  public Integer digitsInteger() {
    Digits digits = field.getAnnotation(Digits.class);
    return digits != null ? digits.integer() : 0;
  }

  @Override
  public Integer digitsFraction() {
    Digits digits = field.getAnnotation(Digits.class);
    return digits != null ? digits.fraction() : 0;
  }

  @Override
  public boolean isRequired() {
    return isAnnotatedWith(NotNull.class.getCanonicalName(), NotEmpty.class.getCanonicalName(), NotBlank.class.getCanonicalName());
  }

  @Override
  public Optional<String> getter() {
    try {
      return Stream.of(Introspector.getBeanInfo(field.getDeclaringClass()).getMethodDescriptors())
          .filter(m -> {
            String capitalizeName = StringUtils.capitalize(field.getName());
            return m.getMethod().getName().equals("get" + capitalizeName)
                || m.getMethod().getName().equals("is" + capitalizeName)
                || m.getMethod().getName().equals("has" + capitalizeName);
          })
          .map(FeatureDescriptor::getName)
          .findAny();
    } catch (IntrospectionException e) {
      throw new JRocksModelException(e.getMessage(), e);
    }
  }

  @Override
  public Optional<String> setter() {
    try {
      return Stream.of(Introspector.getBeanInfo(field.getDeclaringClass()).getMethodDescriptors())
          .filter(m -> m.getMethod().getName().equals("set" + StringUtils.capitalize(field.getName())))
          .map(FeatureDescriptor::getName)
          .findAny();
    } catch (IntrospectionException e) {
      throw new JRocksModelException(e.getMessage(), e);
    }
  }
}
