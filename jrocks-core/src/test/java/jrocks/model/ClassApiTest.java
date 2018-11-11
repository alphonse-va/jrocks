package jrocks.model;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.test.model.Matrix;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

abstract class ClassApiTest {

  ClassApi classApi;

  abstract void before();

  @Test
  abstract void addField() throws NoSuchFieldException;

  @Test
  abstract void getSourceClassPath();

  @Test
  void packageName() {
    assertThat(classApi.packageName()).isEqualTo(Matrix.class.getPackage().getName());
  }

  @Test
  void simpleName() {
    assertThat(classApi.name()).isEqualTo(Matrix.class.getCanonicalName());
  }

  @Test
  void pluralSimpleName() {
    assertThat(classApi.pluralSimpleName()).isEqualTo("Matrices");
  }

  @Test
  void name() {
    assertThat(classApi.name()).isEqualTo(Matrix.class.getCanonicalName());
  }

  @Test
  void propertyName() {
    assertThat(classApi.propertyName()).isEqualTo("matrix");
  }

  @Test
  void pluralPropertyName() {
    assertThat(classApi.pluralPropertyName()).isEqualTo("matrices");
  }

  @Test
  void fields() {
    assertThat(classApi.fields().size()).isEqualTo(6);
  }

  @Test
  void hasRequiredFields() {
    assertThat(classApi.hasRequiredFields()).isTrue();
  }
}