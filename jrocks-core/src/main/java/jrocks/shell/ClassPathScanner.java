package jrocks.shell;

import io.github.classgraph.*;
import jrocks.model.CommandInfo;
import jrocks.model.PluginInfo;
import jrocks.shell.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.apache.commons.lang3.StringUtils.capitalize;

@Component
public class ClassPathScanner {


  private ClassInfoList classes;

  private ScanResult scanResult;

  /**
   * REMEMBER: No constructor parameter here due to circular deps with spring shell bean
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
//            .enableExternalClasses()
//            .enableSystemPackages()
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

  public List<PluginInfo> listInstalledPlugins() {
    File pluginDirectory = configService.globalConfig().getPluginDirectory();

    try {
      List<PluginInfo> result = new ArrayList<>();
      Files.newDirectoryStream(pluginDirectory.toPath(),
          path -> path.toString().endsWith(".jar"))
          .forEach(path -> {
            PluginInfo pluginInfo = new PluginInfo().setJarFile(path.toFile());
            ScanResult pluginsScanResult = new ClassGraph()
                .enableAllInfo()
                .overrideClasspath(path.toFile().getAbsolutePath())
                .scan();
            String jrocksCommandEnum = JRocksCommand.class.getCanonicalName();
            ClassInfoList commandsClassInfo = pluginsScanResult.getClassesWithAnnotation(jrocksCommandEnum);
            for (ClassInfo classInfo : commandsClassInfo) {
              AnnotationInfo annotationInfo = classInfo.getAnnotationInfo().get(jrocksCommandEnum);
              String key = "", description = "", group = "";
              for (AnnotationParameterValue parameter : annotationInfo.getParameterValues()) {
                switch (parameter.getName()) {
                  case "key":
                    key = annValueAsString(parameter.getValue());
                    break;
                  case "value":
                    description = annValueAsString(parameter.getValue());
                    break;
                  case "group":
                    group = annValueAsString(parameter.getValue());
                    break;
                }
              }
              pluginInfo.addCommand(new CommandInfo(key, description, group));
            }
            result.add(pluginInfo);
          });
      return result;
    } catch (IOException e) {
      throw new JRocksShellException("todo", e);
    }

  }

  private String annValueAsString(Object value) {
    String result;
    if (value instanceof String[]) {
      result = Stream.of((String[]) value).collect(Collectors.joining(" "));
    } else if (value instanceof String) {
      result = (String) value;
    } else if (value instanceof Object[]) {
      result = Stream.of((Object[]) value).map(String.class::cast).collect(Collectors.joining(" "));
    } else {
      result = value.toString();
    }
    return result;
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
