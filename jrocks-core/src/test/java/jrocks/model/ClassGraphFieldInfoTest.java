package jrocks.model;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

class ClassGraphFieldInfoTest extends BaseFieldApiTest {

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

    usernameMD = new ClassGraphFieldInfo(classInfo.getFieldInfo("username"));
    passwordMD = new ClassGraphFieldInfo(classInfo.getFieldInfo("password"));
    digitMD = new ClassGraphFieldInfo(classInfo.getFieldInfo("digit"));
    dateMD = new ClassGraphFieldInfo(classInfo.getFieldInfo("date"));
    emailMD = new ClassGraphFieldInfo(classInfo.getFieldInfo("email"));
    decimalMD = new ClassGraphFieldInfo(classInfo.getFieldInfo("decimal"));
  }
}