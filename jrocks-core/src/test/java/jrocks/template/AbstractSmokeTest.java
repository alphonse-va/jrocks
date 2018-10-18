package jrocks.template;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import javax.tools.*;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class AbstractSmokeTest {

  private static StandardJavaFileManager standardJavaFileManager;
  private static final JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();

  @BeforeAll
  static void beforeAll() {
    standardJavaFileManager = javaCompiler.getStandardFileManager(null, null, null);
  }

  @AfterAll
  static void afterAll() throws IOException {
    standardJavaFileManager.close();
  }

  @SafeVarargs
  protected static void assertThatClassCompile(AbstractMap.SimpleEntry<String, String>... entries) {
    String[] options = new String[]{"-d", "/home/fons/dev/git/jrocks/jrocks-core/target"};
    List<JavaSourceFromString> sources = Stream.of(entries).map(source -> new JavaSourceFromString(source.getKey(), source.getValue())).collect(Collectors.toList());
    DiagnosticListener<JavaFileObject> diagnosticListener = e -> {
      assertThat(String.format("%s:%s %s%n", e.getLineNumber(), e.getColumnNumber(), e.getMessage(Locale.ENGLISH))).isNull();
    };
    javaCompiler.getTask(null, null, diagnosticListener, Arrays.asList(options), null, sources).call();
  }
}
