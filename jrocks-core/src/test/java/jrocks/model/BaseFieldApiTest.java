package jrocks.model;

import jrocks.plugin.api.FieldApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

abstract class BaseFieldApiTest {

  FieldApi usernameMD;
  FieldApi passwordMD;
  FieldApi digitMD;
  FieldApi dateMD;
  FieldApi emailMD;
  FieldApi decimalMD;

  @BeforeEach
  abstract void before() throws NoSuchFieldException;

  @Test
  void packageName() {
    assertThat(usernameMD.packageName()).isEqualTo(String.class.getPackage().getName());

  }

  @Test
  void simpleName() {
    assertThat(usernameMD.name()).isEqualTo(String.class.getCanonicalName());
  }

  @Test
  void pluralSimpleName() {
    assertThat(usernameMD.pluralSimpleName()).isEqualTo("Strings");
  }

  @Test
  void entityCanonicalName() {
    assertThat(usernameMD.name()).isEqualTo(String.class.getCanonicalName());
  }

  @Test
  void propertyName() {
    assertThat(usernameMD.propertyName()).isEqualTo("string");
  }

  @Test
  void pluralPropertyName() {
    assertThat(usernameMD.pluralPropertyName()).isEqualTo("strings");
  }

  @Test
  void fieldName() {
    assertThat(usernameMD.fieldName()).isEqualTo("username");
  }

  @Test
  void isAnnotedWith() {
    assertThat(usernameMD.isAnnotatedWith("javax.validation.constraints.Size")).isTrue();
  }

  @Test
  void isNotNull() {
    assertThat(passwordMD.hasNotNull()).isTrue();
  }

  @Test
  void isNotEmpty() {
    assertThat(passwordMD.hasNotEmpty()).isTrue();
  }

  @Test
  void isNotBlank() {
    assertThat(passwordMD.hasNotBlank()).isTrue();
  }

  @Test
  void minSize() {
    assertThat(passwordMD.minSize()).isNull();
    assertThat(usernameMD.minSize()).isEqualTo("10");
    assertThat(digitMD.minSize()).isEqualTo("10");
    assertThat(decimalMD.minSize()).isEqualTo("10.1");
  }

  @Test
  void maxSize() {
    assertThat(passwordMD.maxSize()).isNull();
    assertThat(usernameMD.maxSize()).isEqualTo("20");
    assertThat(digitMD.maxSize()).isEqualTo("100");
    assertThat(decimalMD.maxSize()).isEqualTo("10.1");
  }

  @Test
  void hasDigits() {
    assertThat(digitMD.hasDigits()).isTrue();
  }

  @Test
  void hasEmail() {
    assertThat(emailMD.hasEmail()).isTrue();
  }

  @Test
  void hasFuture() {
    assertThat(dateMD.hasFuture()).isTrue();

  }

  @Test
  void hasNegative() {
    assertThat(digitMD.hasNegative()).isTrue();
  }

  @Test
  void hasNegativeOrZero() {
    assertThat(digitMD.hasNegativeOrZero()).isTrue();
  }

  @Test
  void hasNull() {
    assertThat(digitMD.hasNull()).isTrue();
  }

  @Test
  void hasPast() {
    assertThat(dateMD.hasPast()).isTrue();
  }

  @Test
  void hasPastOrPresent() {
    assertThat(dateMD.hasPastOrPresent()).isTrue();
  }

  @Test
  void hasPattern() {
    assertThat(usernameMD.hasPattern()).isTrue();
  }

  @Test
  void hasPositive() {
    assertThat(digitMD.hasPositive()).isTrue();
  }

  @Test
  void hasPositiveOrZero() {
    assertThat(digitMD.hasPositiveOrZero()).isTrue();
  }

  @Test
  void digitsInteger() {
    assertThat(digitMD.digitsInteger()).isEqualTo(10);
  }

  @Test
  void digitsFraction() {
    assertThat(digitMD.digitsFraction()).isEqualTo(2);
  }

  @Test
  void hasMin() {
    assertThat(digitMD.hasMin()).isTrue();
  }

  @Test
  void hasMax() {
    assertThat(digitMD.hasMax()).isTrue();
  }

  @Test
  void getter() {
    assertThat(digitMD.getter()).isEqualTo(Optional.of("getDigit"));
  }

  @Test
  void setter() {
    assertThat(digitMD.setter()).isEqualTo(Optional.of("setDigit"));
  }
}