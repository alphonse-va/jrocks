package jrocks.shell.value;

import jrocks.shell.JRocksConfig;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClassPathScannerTest {

  private static ClassPathScanner scanner;

  @BeforeAll
  static void beforeAll() {
    scanner = new ClassPathScanner(new JRocksConfig().setBasePackage("jrocks.samples"));
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
}