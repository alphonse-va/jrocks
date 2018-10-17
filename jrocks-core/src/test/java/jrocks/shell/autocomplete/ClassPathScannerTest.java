package jrocks.shell.autocomplete;

import jrocks.ClassPathScanner;
import jrocks.shell.JRocksConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ClassPathScannerTest {

  private static ClassPathScanner scanner;

  private String testFieldWithGetterAndSetter;
  private String testFieldWithGetter;
  private String testFieldWithSetter;

  @BeforeAll
  static void beforeAll() {
    scanner = new ClassPathScanner(new JRocksConfig().setBasePackage("jrocks"));
  }

  @BeforeEach
  void before() {
    scanner.rebuid();
  }

  @Test
  void getAllClassesWithDefaultContructor() {
    assertThat(scanner.getAllClassesWithZeroArgsConstructor())
        .contains("jrocks.samples.model.Matrix")
        .doesNotContain("jrocks.samples.model.MatrixWithoutDefaultConstructor");
  }

  @Test
  void getAllFieldsWithGetterAndSetters() {
    final List<String> actual = scanner.getAllFieldsWithGetterAndSetters(ClassPathScannerTest.class.getCanonicalName());
    assertThat(actual).containsOnly("testFieldWithGetterAndSetter");
  }

  public String getTestFieldWithGetterAndSetter() {
    return testFieldWithGetterAndSetter;
  }

  public ClassPathScannerTest setTestFieldWithGetterAndSetter(final String testFieldWithGetterAndSetter) {
    this.testFieldWithGetterAndSetter = testFieldWithGetterAndSetter;
    return this;
  }

  public String getTestFieldWithGetter() {
    return testFieldWithGetter;
  }

  public ClassPathScannerTest setTestFieldWithSetter(final String testFieldWithSetter) {
    this.testFieldWithSetter = testFieldWithSetter;
    return this;
  }
}