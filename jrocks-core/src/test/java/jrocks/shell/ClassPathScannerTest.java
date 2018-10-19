package jrocks.shell;

import io.github.classgraph.ClassInfo;
import jrocks.mock.TerminalLoggerMock;
import jrocks.shell.config.JRocksProjectConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Collectors;
import java.util.stream.Stream;

class ClassPathScannerTest {

  private ClassPathScanner classPathScanner;
  private JRocksProjectConfig projectConfig;

  @BeforeEach
  void beforeEach() {
    projectConfig = new JRocksProjectConfig();
    projectConfig.addOutputDirectory("./target/classes");
    projectConfig.addSourceDirectory("./src/main/java");
    projectConfig.addBuildDirectory("./target");
    projectConfig.setBasePackage("jrocks");
    projectConfig.setInitialized(true);

    classPathScanner = new ClassPathScanner(projectConfig);
    classPathScanner.setTerminalLogger(new TerminalLoggerMock());
    classPathScanner.rebuild();
  }

  @Test
  void getAllClassesWithZeroArgsConstructor() {
  }

  @Test
  void getAllClasses() {
  }

  @Test
  void getAllClassInfo() {
    Stream<ClassInfo> allClassInfo = classPathScanner.getAllClassInfo();
    classPathScanner.getAllClassInfo().filter(ci -> ci.getName().endsWith("JRocksApplication")).map(ClassInfo::getClasspathElementFile).collect(Collectors.toList());
    Assertions.assertThat(allClassInfo).isNotEmpty();
  }

  @Test
  void getAllFieldsWithSetters() {
  }

  @Test
  void getAllFieldsWithGetters() {
  }

  @Test
  void getAllFieldsWithGetterAndSetters() {
  }

  @Test
  void setTerminalLogger() {
  }
}