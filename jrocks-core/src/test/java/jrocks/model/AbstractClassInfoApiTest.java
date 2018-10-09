package jrocks.model;

import org.junit.jupiter.api.Test;

class AbstractClassInfoApiTest {

  @Test
  void removeSuffix() {
    "testDto".replaceAll("(Dto$)", "");
  }

}