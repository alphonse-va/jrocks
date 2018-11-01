package jrocks.model;

import io.github.classgraph.AnnotationInfo;
import io.github.classgraph.AnnotationParameterValue;
import io.github.classgraph.ClassRefTypeSignature;
import io.github.classgraph.FieldInfo;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

public class ClassGraphFieldInfo extends ClassGraphClassInfo implements FieldClassInfo {

  private FieldInfo fieldInfo;

  ClassGraphFieldInfo(FieldInfo fieldInfo) {
    super(((ClassRefTypeSignature) fieldInfo.getTypeDescriptor()).getClassInfo());
    this.fieldInfo = fieldInfo;
  }

  @Override
  public String fieldName() {
    return fieldInfo.getName();
  }

  @Override
  public String fieldNameCapitalized() {
    return StringUtils.capitalize(fieldInfo.getName());
  }

  @Override
  public boolean isAnnotatedWith(String... annotationClasses) {
    return Stream.of(annotationClasses).anyMatch(className -> fieldInfo.getAnnotationInfo().containsName(className));
  }

  @Override
  public boolean hasNotNull() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.NotNull");
  }

  @Override
  public boolean hasNotEmpty() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.NotEmpty");
  }

  @Override
  public boolean hasNotBlank() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.NotBlank");
  }

  @Override
  public boolean hasDigits() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.Digits");
  }

  @Override
  public boolean hasEmail() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.Email");
  }

  @Override
  public boolean hasFuture() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.Future");
  }

  @Override
  public boolean hasMax() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.Max");
  }

  @Override
  public boolean hasMin() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.Min");
  }

  @Override
  public boolean hasNegative() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.Negative");
  }

  @Override
  public boolean hasNegativeOrZero() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.NegativeOrZero");
  }

  @Override
  public boolean hasNull() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.Null");
  }

  @Override
  public boolean hasPast() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.Past");
  }

  @Override
  public boolean hasPastOrPresent() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.PastOrPresent");
  }

  @Override
  public boolean hasPattern() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.Pattern");
  }

  @Override
  public boolean hasPositive() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.Positive");
  }

  @Override
  public boolean hasPositiveOrZero() {
    return fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.PositiveOrZero");
  }

  @Override
  public String maxSize() {
    AnnotationInfo sizeAnnotation = fieldInfo.getAnnotationInfo().get("javax.validation.constraints.Size");
    AnnotationInfo maxAnnotation = fieldInfo.getAnnotationInfo().get("javax.validation.constraints.Max");
    AnnotationInfo decimalMaxAnnotation = fieldInfo.getAnnotationInfo().get("javax.validation.constraints.DecimalMax");

    String size = getAnnotationParameterValueOrNull(sizeAnnotation, "max", String::valueOf);
    String decimalMax = getAnnotationParameterValueOrNull(decimalMaxAnnotation, "value", String::valueOf);
    String max = getAnnotationParameterValueOrNull(maxAnnotation, "value", String::valueOf);

    return size != null ? size : decimalMax != null ? decimalMax : max;
  }

  @Override
  public String minSize() {
    AnnotationInfo sizeAnnotation = fieldInfo.getAnnotationInfo().get("javax.validation.constraints.Size");
    AnnotationInfo minAnnotation = fieldInfo.getAnnotationInfo().get("javax.validation.constraints.Min");
    AnnotationInfo decimalMinAnnotation = fieldInfo.getAnnotationInfo().get("javax.validation.constraints.DecimalMin");

    String size = getAnnotationParameterValueOrNull(sizeAnnotation, "min", String::valueOf);
    String decimalMin = getAnnotationParameterValueOrNull(decimalMinAnnotation, "value", String::valueOf);
    String min = getAnnotationParameterValueOrNull(minAnnotation, "value", String::valueOf);

    return size != null ? size : decimalMin != null ? decimalMin : min;
  }

  @Nullable
  private <T> T getAnnotationParameterValueOrNull(AnnotationInfo annotationInfo, String parameterName, Function<Object, T> converter) {
    if (annotationInfo == null) return null;
    return annotationInfo.getParameterValues()
        .stream().filter(ap -> ap.getName().equals(parameterName))
        .map(AnnotationParameterValue::getValue)
        .filter(Objects::nonNull)
        .map(converter)
        .findAny()
        .orElse(null);
  }


  @Override
  public Integer digitsInteger() {
    AnnotationInfo digits = fieldInfo.getAnnotationInfo().get("javax.validation.constraints.Digits");
    return getAnnotationParameterValueOrNull(digits, "integer", Integer.class::cast);
  }

  @Override
  public Integer digitsFraction() {
    AnnotationInfo digits = fieldInfo.getAnnotationInfo().get("javax.validation.constraints.Digits");
    return getAnnotationParameterValueOrNull(digits, "fraction", Integer.class::cast);
  }

  @Override
  public boolean isRequired() {
    return fieldInfo.getAnnotationInfo().containsName("jrocks.generator.bean.annotations.BuilderProperty")
        || fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.NotNull")
        || fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.NotEmpty")
        || fieldInfo.getAnnotationInfo().containsName("javax.validation.constraints.NotBlank");
  }

  @Override
  public String getter() {
    return fieldInfo.getDefiningClassInfo().getDeclaredMethodInfo().getNames().stream()
        .filter(m -> {
          String capitalizeName = StringUtils.capitalize(fieldInfo.getName());
          return m.equals("get" + capitalizeName) || m.equals("is" + capitalizeName) || m.equals("has" + capitalizeName);
        }).findAny()
        .orElseThrow(() -> new IllegalStateException("fieldInfo '" + fieldName() + "' need a getter!"));
  }

  @Override
  public String setter() {
    return fieldInfo.getDefiningClassInfo().getDeclaredMethodInfo().getNames().stream()
        .filter(m -> m.equals("set" + StringUtils.capitalize(fieldInfo.getName())))
        .findAny()
        .orElseThrow(() -> new IllegalStateException("fieldInfo '" + fieldName() + "' need a setter!"));
  }
}
