package jrocks.model;

import jrocks.Matrix;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.constraints.Size;

import static org.assertj.core.api.Assertions.assertThat;

class FieldMetaDataTest {

  private FieldMetaData<String> usernameMD;
  private FieldMetaData<String> passwordMD;
  private FieldMetaData<Object> digitMD;
  private FieldMetaData<Object> dateMD;
  private FieldMetaData<Object> emailMD;
  private FieldMetaData<Object> decimalMD;

  @BeforeEach
  void before() {
    usernameMD = new FieldMetaData<>(FieldUtils.getField(Matrix.class, "username", true));
    passwordMD = new FieldMetaData<>(FieldUtils.getField(Matrix.class, "password", true));
    digitMD = new FieldMetaData<>(FieldUtils.getField(Matrix.class, "digit", true));
    dateMD = new FieldMetaData<>(FieldUtils.getField(Matrix.class, "date", true));
    emailMD = new FieldMetaData<>(FieldUtils.getField(Matrix.class, "email", true));
    decimalMD = new FieldMetaData<>(FieldUtils.getField(Matrix.class, "decimal", true));
  }

  @Test
  void packageName() {
    assertThat(usernameMD.packageName()).isEqualTo(String.class.getPackage().getName());

  }

  @Test
  void simpleName() {
    assertThat(usernameMD.simpleName()).isEqualTo(String.class.getSimpleName());
  }

  @Test
  void pluralSimpleName() {
    assertThat(usernameMD.pluralSimpleName()).isEqualTo("Strings");
  }

  @Test
  void entityCanonicalName() {
    assertThat(usernameMD.canonicalName()).isEqualTo(String.class.getCanonicalName());
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
  void getEntityClass() {
    assertThat(usernameMD.getBeanClass()).isEqualTo(String.class);
  }

  @Test
  void fieldName() {
    assertThat(usernameMD.fieldName()).isEqualTo("username");
  }

  @Test
  void fieldAnnotations() {
    assertThat(usernameMD.fieldAnnotations().findAny()).isPresent();
  }

  @Test
  void isAnnotedWith() {
    assertThat(usernameMD.isAnnotatedWith(Size.class)).isTrue();
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
    assertThat(digitMD.getter()).isEqualTo("getDigit");
  }

  @Test
  void setter() {
    assertThat(digitMD.setter()).isEqualTo("setDigit");
  }
}