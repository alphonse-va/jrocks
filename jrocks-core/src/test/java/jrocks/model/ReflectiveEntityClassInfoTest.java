package jrocks.model;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import jrocks.samples.model.Matrix;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReflectiveEntityClassInfoTest {

  private static ClassInfoList classes;
  private EntityClassInfo md;


  @BeforeAll
  static void beforeAll() {
    ScanResult scanResult = new ClassGraph()
        .enableAllInfo()
        .whitelistPackages("jrocks")
        .scan();
    classes = scanResult.getAllStandardClasses();
  }

  @BeforeEach
  void before() {
    ClassInfo matrixClassInfo = classes.get("jrocks.samples.model.Matrix");
    ReflectiveClassInfo longClassInfo = new ReflectiveClassInfo(Long.class);
    md = new ReflectiveEntityClassInfo(matrixClassInfo, longClassInfo);
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