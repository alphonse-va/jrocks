package jrocks.plugin.test;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class BasePluginIntegrationTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(BasePluginIntegrationTest.class);

  private static StandardJavaFileManager standardJavaFileManager;
  private static final JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
  private static Path tmpDir;

  @BeforeAll
  static void beforeAll() {
    standardJavaFileManager = javaCompiler.getStandardFileManager(null, null, null);
    try {
      tmpDir = Files.createTempDirectory("jrocks-smoke-test-");
    } catch (IOException e) {
      throw new IllegalStateException("Error while creating temp directory", e);
    }
  }

  @AfterAll
  static void afterAll() throws IOException {
    if (standardJavaFileManager != null) standardJavaFileManager.close();
    delete(tmpDir.toFile());
  }

  @SafeVarargs
  protected static void assertThatClassCompile(AbstractMap.SimpleEntry<String, String>... entries) {

    DiagnosticListener<JavaFileObject> diagnosticListener = e -> {
      Stream.of(entries).forEach(entry -> LOGGER.error("\nClass: {}\nSource: \n{}", entry.getKey(), entry.getValue()));
      Assertions.assertThat(String.format("%s:%s %s%n", e.getLineNumber(), e.getColumnNumber(), e.getMessage(Locale.ENGLISH))).isNull();
    };

    String[] options = new String[]{"-d", tmpDir.toFile().getAbsolutePath()};
    List<JavaSourceFromString> sources = Stream.of(entries).map(source -> new JavaSourceFromString(source.getKey(), source.getValue())).collect(Collectors.toList());

    javaCompiler.getTask(null, null, diagnosticListener, Arrays.asList(options), null, sources).call();
  }

  private static class JavaSourceFromString extends SimpleJavaFileObject {

    final String code;

    JavaSourceFromString(String name, String code) {
      super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension),
          Kind.SOURCE);
      this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
      return code;
    }
  }

  private static void delete(File tempDir) throws IOException {
    if (tempDir.isDirectory())
      for (File c : Objects.requireNonNull(tempDir.listFiles()))
        delete(c);
    if (!tempDir.delete()) throw new FileNotFoundException("Enable to delete file: " + tempDir);
  }
}
