package jrocks.model;

import jrocks.plugin.test.model.Matrix;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import java.util.stream.Stream;

class ReflectiveClassApiTest extends ClassApiTest {

  @BeforeEach
  @Override
  void before() {
    classApi = new ReflectiveClassInfo(Matrix.class);
    Stream.of(Matrix.class.getDeclaredFields())
        .forEach(f -> classApi.addField(new ReflectiveFieldInfo(f)));
  }

  @Override
  void addField() throws NoSuchFieldException {
    int nbField = classApi.fields().size();
    classApi.addField(new ReflectiveFieldInfo(Matrix.class.getDeclaredField("password")));

    Assertions.assertThat(classApi.fields().size()).isEqualTo(nbField + 1);
  }

  @Override
  void getSourceClassPath() {
    // TODO
  }
}
