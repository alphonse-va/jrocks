package jrocks;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import jrocks.shell.JRocksConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.capitalize;

@Component
public class ClassPathScanner {

  private final JRocksConfig jRocksConfig;

  private ClassInfoList classes;

  private ScanResult scanResult;

  @Autowired
  public ClassPathScanner(JRocksConfig jRocksConfig) {
    this.jRocksConfig = jRocksConfig;
  }

  @PostConstruct
  public void rebuid() {
    scanResult = new ClassGraph()
        .enableAllInfo()
        .whitelistPackages(jRocksConfig.getBasePackage())
        .scan();
    classes = scanResult.getAllStandardClasses();
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

  private Stream<String> getFieldNames(final String className) {
    return classes.get(className).getDeclaredFieldInfo().getNames().stream();
  }

  private boolean isMethodNameExist(final String className, final String set, final String fieldName) {
    return !classes.get(className).getMethodInfo().get(getMethodName(set, fieldName)).isEmpty();
  }

  private static String getMethodName(final String prefix, final String fieldName) {
    return prefix + capitalize(fieldName);
  }

  /**
   * TODO: We could use an aspect to call this method
   */
  private void rebuildIfNeeded() {
    if (jRocksConfig.isAutoRebuild()) {
      rebuid();
    } else if (scanResult.classpathContentsModifiedSinceScan()) {
      rebuid();
    }
  }
}
