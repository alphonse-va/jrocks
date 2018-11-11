package jrocks.model;

import jrocks.plugin.test.model.Matrix;
import org.junit.jupiter.api.BeforeEach;

class ReflectiveFieldApiTest extends BaseFieldApiTest {

  @BeforeEach
  void before() throws NoSuchFieldException {
    usernameMD = new ReflectiveFieldInfo(Matrix.class.getDeclaredField("username"));
    passwordMD = new ReflectiveFieldInfo(Matrix.class.getDeclaredField("password"));
    digitMD = new ReflectiveFieldInfo(Matrix.class.getDeclaredField("digit"));
    dateMD = new ReflectiveFieldInfo(Matrix.class.getDeclaredField("date"));
    emailMD = new ReflectiveFieldInfo(Matrix.class.getDeclaredField("email"));
    decimalMD = new ReflectiveFieldInfo(Matrix.class.getDeclaredField("decimal"));
  }
}