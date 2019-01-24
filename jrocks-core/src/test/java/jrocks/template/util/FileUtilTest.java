package jrocks.template.util;

import jrocks.plugin.api.shell.TerminalLogger;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Files;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;

@ExtendWith(MockitoExtension.class)
class FileUtilTest {

  private File target;

  @BeforeEach
  void before() throws IOException {
    target = Files.newTemporaryFile();
    java.nio.file.Files.write(target.toPath(), "before\n// @@jrocks.test-marker@@\nafter".getBytes());
  }

  @Test
  void addContentAfterMarker(@Mock TerminalLogger logger) throws IOException {
    File actual = new FileUtil(logger).addContentAfterMarker(target.getAbsolutePath(), "between", "@@jrocks.test-marker@@").get();
    Assertions.assertThat(java.nio.file.Files.readAllLines(actual.toPath())).contains("between");
  }
}