//package jrocks.shell.autocomplete;
//
//import jrocks.ClassPathScanner;
//import jrocks.shell.JLineTerminalLogger;
//import jrocks.shell.JRocksConfig;
//import jrocks.shell.JRocksProjectConfig;
//import jrocks.shell.command.JRocksCommandStatus;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//class ClassPathScannerTest {
//
//  private static ClassPathScanner scanner;
//
//  private String testFieldWithGetterAndSetter;
//  private String testFieldWithGetter;
//  private String testFieldWithSetter;
//
//  @BeforeAll
//  static void beforeAll() {
//    JRocksProjectConfig jRocksConfig = new JRocksConfig()
//        .setBasePackage("jrocks.samples")
//        .setOutputDirectory("/home/fons/dev/git/jrocks/jrocks-core/target/classes");
//    scanner = new ClassPathScanner(jRocksConfig, new JRocksCommandStatus().setInititalized(true));
//    scanner.setTerminalLogger(new JLineTerminalLogger());
//  }
//
//  @BeforeEach
//  void before() {
//    scanner.rebuild();
//  }
//
//  @Test
//  void getAllClassesWithDefaultContructor() {
//    assertThat(scanner.getAllClassesWithZeroArgsConstructor())
//        .contains("jrocks.samples.model.Matrix")
//        .doesNotContain("jrocks.samples.model.MatrixWithoutDefaultConstructor");
//  }
//
//  //@Test
//  void getAllFieldsWithGetterAndSetters() {
//    List<String> actual = scanner.getAllFieldsWithGetterAndSetters(ClassPathScannerTest.class.getCanonicalName());
//    assertThat(actual).containsOnly("testFieldWithGetterAndSetter");
//  }
//}