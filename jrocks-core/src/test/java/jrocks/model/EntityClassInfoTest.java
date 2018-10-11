package jrocks.model;

import jrocks.api.EntityClassInfoApi;
import jrocks.samples.model.Matrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EntityClassInfoTest {

  private EntityClassInfoApi md;

  @BeforeEach
  void before() {
    md = new EntityClassInfo(Matrix.class, Long.class);
  }

  @Test
  void packageName() {
    assertThat(md.packageName()).isEqualTo(Matrix.class.getPackage().getName());

  }

  @Test
  void simpleName() {
    assertThat(md.simpleName()).isEqualTo(Matrix.class.getSimpleName());
  }

  @Test
  void entityPluralSimpleName() {
    assertThat(md.pluralSimpleName()).isEqualTo("Matrices");
  }

  @Test
  void entityCanonicalName() {
    assertThat(md.canonicalName()).isEqualTo(Matrix.class.getCanonicalName());
  }

  @Test
  void idCanonicalName() {
    assertThat(md.idCanonicalName()).isEqualTo(Long.class.getCanonicalName());
  }

  @Test
  void idSimpleName() {
    assertThat(md.idSimpleName()).isEqualTo(Long.class.getSimpleName());
  }

  @Test
  void propertyName() {
    assertThat(md.propertyName()).isEqualTo("matrix");
  }

  @Test
  void entityPluralPropertyName() {
    assertThat(md.pluralPropertyName()).isEqualTo("matrices");
  }

  @Test
  void restPath() {
    assertThat(md.restPath()).isEqualTo("matrix");
  }
}