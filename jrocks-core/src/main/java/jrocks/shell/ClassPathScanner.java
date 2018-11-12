package jrocks.shell;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import jrocks.shell.config.ConfigService;
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


  private ClassInfoList classes;

  private ScanResult scanResult;

  /**
   * REMEMBER: No constructor parameter here due to circular deps with spring shell beans
   */
  private final TerminalLogger terminalLogger;

  private final ConfigService configService;

  @Autowired
  public ClassPathScanner(ConfigService configService, TerminalLogger terminalLogger) {
    this.configService = configService;
    this.terminalLogger = terminalLogger;
  }

  @PostConstruct
  public void rebuild() {
    if (configService.isInitialized()) {

      String basePackage = configService.getConfig().getBasePackage();
      URL[] outputDirectoriesAsURLs = configService.getConfig().getOutputDirectoriesAsURLs();
      try {

        scanResult = new ClassGraph()
            .enableAllInfo()
            .overrideClassLoaders(new URLClassLoader(outputDirectoriesAsURLs))
            .whitelistPackages(basePackage)
            .scan();
      } catch (IllegalArgumentException e) {
        /*
          Fail back without base package

          REMEMBER: ClassGraph can throw IllegalArgumentException: This style of whitelisting/blacklisting is no longer
          supported for invalid whitelistPackages

          */
        terminalLogger.warning("basePackage '%s' configuration parameter seems to be malformed", basePackage);
        terminalLogger.warning("Please update your configuration:\n\n%s", configService.getConfig());
        scanResult = new ClassGraph()
            .enableAllInfo()
            .overrideClassLoaders(new URLClassLoader(outputDirectoriesAsURLs))
            .scan();
      }
      classes = scanResult.getAllStandardClasses();
    } else {
      terminalLogger.verbose("Classpath scanning skipped, JRocks is not yet initialized!");
    }
  }

  public Stream<ClassInfo> getAllClassInfo() {
    rebuildIfNeeded();
    return classes.stream();
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


  /**
   * TODO: We could use an aspect to call this method
   */
  private void rebuildIfNeeded() {
    if (configService.getConfig().isAutoRebuild()) {
      rebuild();
    } else if (scanResult.classpathContentsModifiedSinceScan()) {
      rebuild();
    }
  }
}
