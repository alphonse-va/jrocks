package jrocks.shell.config;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

class BaseMavenServiceTest {

  private BaseMavenService baseMavenService;

  @BeforeEach
  void beforeEach() {
    baseMavenService = new BaseMavenService();
  }

  @Test
  void effectivePomToListOfPoms() throws IOException {
    String testFilePath = getClass().getClassLoader().getResource("effective-pom.txt").getPath();
    Path path = Paths.get(testFilePath);
    List<String> poms = baseMavenService.splitPoms(String.join("\n", Files.readAllLines(path)));
    Assertions.assertThat(poms).size().isEqualTo(3);
  }
}