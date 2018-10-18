package jrocks;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import jrocks.shell.JRocksProjectConfig;
import jrocks.shell.TerminalLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.capitalize;

@Component
public class ClassPathScanner {

  private final JRocksProjectConfig projectConfig;

  private ClassInfoList classes;

  private ScanResult scanResult;

  /**
   * REMEMBER: Not constructor parameter due to circular deps mess
   */
  @Autowired
  private TerminalLogger terminalLogger;

  @Autowired
  public ClassPathScanner(JRocksProjectConfig projectConfig) {
    this.projectConfig = projectConfig;
  }

  @PostConstruct
  public void rebuild() {
    if (projectConfig.isInitialized()) {
      scanResult = new ClassGraph()
          .enableAllInfo()
          .whitelistPaths(projectConfig.getOutputDirectory())
          .whitelistPackages(projectConfig.getBasePackage())
          .scan();
      classes = scanResult.getAllStandardClasses();

      terminalLogger.verbose("Classes available for completion\n");
      classes.forEach(cl -> terminalLogger.verbose(cl.getName()));
      terminalLogger.verbose("\n\n");
    } else {
      terminalLogger.verbose("Class path scanning skipped, JRocks is not yet initialized!");
    }
  }

  public List<String> getAllClassesWithZeroArgsConstructor() {
    rebuildIfNeeded();
    return classes.stream()
        .filter(ci -> ci.getConstructorInfo().stream().anyMatch(c -> c.getParameterInfo().length == 0))
        .map(ClassInfo::getName)
        .collect(Collectors.toList());
  }

  public List<String> getAllClasses() {
    rebuildIfNeeded();
    return classes.stream()
        .map(ClassInfo::getName)
        .collect(Collectors.toList());
  }

  public List<String> getAllFieldsWithSetters(String className) {
    rebuildIfNeeded();
    return getFieldNames(className)
        .filter(fieldName -> isMethodNameExist(className, "set", fieldName))
        .collect(Collectors.toList());
  }

  public List<String> getAllFieldsWithGetters(String className) {
    rebuildIfNeeded();
    return getFieldNames(className)
        .filter(fieldName -> isMethodNameExist(className, "get", fieldName))
        .collect(Collectors.toList());
  }

  public List<String> getAllFieldsWithGetterAndSetters(String className) {
    rebuildIfNeeded();
    return getFieldNames(className)
        .filter(fieldName ->
            isMethodNameExist(className, "set", fieldName)
                && (isMethodNameExist(className, "get", fieldName)
                || isMethodNameExist(className, "is", fieldName)))
        .collect(Collectors.toList());
  }

  private Stream<String> getFieldNames(String className) {
    return classes.get(className).getDeclaredFieldInfo().getNames().stream();
  }

  private boolean isMethodNameExist(String className, String set, String fieldName) {
    return !classes.get(className).getMethodInfo().get(getMethodName(set, fieldName)).isEmpty();
  }

  private static String getMethodName(String prefix, String fieldName) {
    return prefix + capitalize(fieldName);
  }

  public ClassPathScanner setTerminalLogger(TerminalLogger terminalLogger) {
    this.terminalLogger = terminalLogger;
    return this;
  }

  /**
   * TODO: We could use an aspect to call this method
   */
  private void rebuildIfNeeded() {
    if (projectConfig.isAutoRebuild()) {
      rebuild();
    } else if (scanResult.classpathContentsModifiedSinceScan()) {
      rebuild();
    }
  }
}
