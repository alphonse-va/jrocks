package jrocks.model;

import org.junit.jupiter.api.Test;

class AbstractMetaDataTest {

  @Test
  void removeSuffix() {
    "testDto".replaceAll("(Dto$)", "");
  }

}