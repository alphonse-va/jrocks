package jrocks.model;

import jrocks.api.ClassInfoApi;
import jrocks.samples.model.Matrix;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BeanBeanClassInfoBuilderTest {

  @Test
  void build() {
    final ClassInfoApi classInfo = new BeanClassInfoBuilder<>(Matrix.class).build();
    assertThat(classInfo.getFields()).size().isEqualTo(6);
  }
}