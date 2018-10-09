package jrocks.model;

import jrocks.Matrix;
import jrocks.api.ClassInfoApi;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BeanClassInfoBuilderTest {

  @Test
  void build() {
    final ClassInfoApi metaData = new BeanMetaDataBuilder(Matrix.class).build();
    assertThat(metaData.getFields()).size().isEqualTo(6);
  }
}