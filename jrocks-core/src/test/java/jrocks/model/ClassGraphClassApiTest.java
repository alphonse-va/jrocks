package jrocks.model;

import io.github.classgraph.*;
import jrocks.plugin.test.model.Matrix;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

class ClassGraphClassApiTest extends BaseClassApiTest {

  private static ClassInfoList classes;

  @BeforeAll
  static void beforeAll() {
    ScanResult scanResult = new ClassGraph()
        .enableAllInfo()
        .enableSystemPackages()
        .scan();
    classes = scanResult.getAllStandardClasses();
  }

  @BeforeEach
  void before() {
    ClassInfo classInfo = classes.get("jrocks.plugin.test.model.Matrix");
    classApi = new ClassGraphClassInfo(classInfo);
    classInfo.getDeclaredFieldInfo().forEach(fieldInfo -> classApi.addField(new ClassGraphFieldInfo(fieldInfo)));

  }

  @Override
  void addField() throws NoSuchFieldException {
    int nbField = classApi.fields().size();
    classApi.addField(new ReflectiveFieldInfo(Matrix.class.getDeclaredField("password")));

    Assertions.assertThat(classApi.fields().size()).isEqualTo(nbField + 1);
  }

  @Override
  void getSourceClassPath() {
    Assertions.assertThat(classApi.getSourceClassPath()).exists();
  }
}
