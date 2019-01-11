package jrocks.shell;

import io.github.classgraph.ClassInfo;
import jrocks.plugin.api.config.ConfigService;
import jrocks.plugin.api.config.ModuleConfig;
import jrocks.plugin.api.config.ProjectConfig;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClassPathScannerTest {

  private ClassPathScanner classPathScanner;

  @BeforeEach
  void beforeEach(@Mock ConfigService configService) {
    ProjectConfig projectConfig = new ProjectConfig();

    projectConfig
        .setBasePackage("jrocks")
        .setAutoRebuild(true)
        .addModule(new ModuleConfig()
            .setOutputDirectory("./target/classes")
            .setSourceDirectory("./src/main/java"));

    when(configService.getConfig()).thenReturn(projectConfig);
    when(configService.isInitialized()).thenReturn(true);

    classPathScanner = new ClassPathScanner(configService, mock(TerminalLogger.class));
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