package jrocks.template;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;

import javax.tools.*;
import java.io.IOException;
import java.net.URI;
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
  protected static ClassInfoList classes;

  @BeforeAll
  static void beforeAll() {
    standardJavaFileManager = javaCompiler.getStandardFileManager(null, null, null);
    ScanResult scanResult = new ClassGraph()
        .enableAllInfo()
        .whitelistPackages("jrocks")
        .scan();
    classes = scanResult.getAllStandardClasses();
  }

  @AfterAll
  static void afterAll() throws IOException {
    if (standardJavaFileManager != null) standardJavaFileManager.close();
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

  private static class JavaSourceFromString extends SimpleJavaFileObject {
    /**
     * The source code of this "file".
     */
    final String code;

    /**
     * Constructs a new JavaSourceFromString.
     * @param name the name of the compilation unit represented by this file object
     * @param code the source code for the compilation unit represented by this file object
     */
    public JavaSourceFromString(String name, String code) {
      super(URI.create("string:///" + name.replace('.','/') + Kind.SOURCE.extension),
          Kind.SOURCE);
      this.code = code;
    }

    @Override
    public CharSequence getCharContent(boolean ignoreEncodingErrors) {
      return code;
    }
  }
}
