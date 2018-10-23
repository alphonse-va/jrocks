package jrocks.shell;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import jrocks.shell.config.JRocksProjectConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.net.URL;
import java.net.URLClassLoader;
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
   * REMEMBER: No constructor parameter here due to circular deps with spring shell bean
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

      String basePackage = projectConfig.getBasePackage();
      try {

        scanResult = new ClassGraph()
            .enableAllInfo()
//            .enableExternalClasses()
//            .enableSystemPackages()
            .overrideClassLoaders(new URLClassLoader(projectConfig.getOutputDirectoriesAsURLs().toArray(new URL[0])))
            .whitelistPackages(basePackage)
            .scan();
      } catch (IllegalArgumentException e) {
        /*
          Fail back without base package

          REMEMBER: ClassGraph can throw IllegalArgumentException: This style of whitelisting/blacklisting is no longer
          supported for invalid whitelistPackages

          */
        terminalLogger.error("basePackage '%s' configuration parameter seems to be malformed", basePackage);
        terminalLogger.warning("Please update your configuration:\n\n%s", projectConfig.toString());
        scanResult = new ClassGraph()
            .enableAllInfo()
            .overrideClassLoaders(new URLClassLoader(projectConfig.getOutputDirectoriesAsURLs().toArray(new URL[0])))
            .scan();
      }
      classes = scanResult.getAllStandardClasses();
    } else {
      terminalLogger.warning("Classpath scanning skipped, JRocks is not yet initialized!");
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

  public Stream<ClassInfo> getAllClassInfo() {
    rebuildIfNeeded();
    return classes.stream();
  }

  public List<String> getAllFieldsWithSetters(String className) {
    rebuildIfNeeded();
    return getFieldNames(className)
        .filter(fieldName -> doesMethodExist(className, "set", fieldName))
        .collect(Collectors.toList());
  }

  public List<String> getAllFieldsWithGetters(String className) {
    rebuildIfNeeded();
    return getFieldNames(className)
        .filter(fieldName -> doesMethodExist(className, "get", fieldName))
        .collect(Collectors.toList());
  }

  public List<String> getAllFieldsWithGetterAndSetters(String className) {
    rebuildIfNeeded();
    return getFieldNames(className)
        .filter(fieldName ->
            doesMethodExist(className, "set", fieldName)
                && (doesMethodExist(className, "get", fieldName)
                || doesMethodExist(className, "is", fieldName)))
        .collect(Collectors.toList());
  }

  private Stream<String> getFieldNames(String className) {
    return classes.get(className).getDeclaredFieldInfo().getNames().stream();
  }

  private boolean doesMethodExist(String className, String methodName, String fieldName) {
    return !classes.get(className).getMethodInfo().get(getMethodName(methodName, fieldName)).isEmpty();
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
