package jrocks.model;

import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.utils.ReflectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.validation.constraints.*;
import java.beans.FeatureDescriptor;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.MethodDescriptor;
import java.lang.reflect.Field;
import java.util.stream.Stream;

public class ReflectiveFieldInfo extends ReflectiveClassInfo implements FieldClassInfo {

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
  public Stream<AnnotationInfo> fieldAnnotations() {
    // TODO
    //return Stream.of(field.getAnnotations());
    return null;
  }

  @Override
  public boolean isAnnotatedWith(String... annotationClasses) {
    return Stream.of(annotationClasses).anyMatch(className -> ReflectionUtils.classForNameOrNull(className) != null);
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
  public String getter() {
    try {
      return Stream.of(Introspector.getBeanInfo(field.getDeclaringClass()).getMethodDescriptors())
          .filter(m -> {
            String capitalizeName = StringUtils.capitalize(field.getName());
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
    Class<?>[] parameterTypes = m.getMethod().getParameterTypes();
    if (parameterTypes.length > 1) {
      return false;
    }
    boolean hasSetterParamType = Stream.of(parameterTypes).anyMatch(t -> t.equals(field.getType()));
    return hasSetterParamType && !m.isHidden() && m.getName().equals("set" + fieldNameCapitalized());
  }

  private boolean isGetter(MethodDescriptor m) {
    boolean hasReturnType = m.getMethod().getReturnType().equals(field.getType());
    return hasReturnType && !m.isHidden()
        && (m.getName().equals("get" + fieldNameCapitalized())
        || m.getName().equals("is" + fieldNameCapitalized())
        || m.getName().equals("has" + fieldNameCapitalized()));
  }
}
