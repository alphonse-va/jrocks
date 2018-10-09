package jrocks.model;

import jrocks.Matrix;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BeanMetaDataBuilderTest {

  @Test
  void build() {
    final BeanMetaData<Matrix> metaData = new BeanMetaDataBuilder<>(Matrix.class).build();
    assertThat(metaData.getFields()).size().isEqualTo(6);
  }
}