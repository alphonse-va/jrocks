package jrocks.shell.valueproviders;

import io.github.classgraph.*;
import jrocks.shell.JRocksConfig;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

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

  /*
   * returns setters
   */
  public List<String> getMutableFields(String className) {
    return classes.get(className).getDeclaredFieldInfo().getNames().stream()
        .filter(fieldName -> {
          final String setter = "set" + capitalize(fieldName);
          return classes.get(className).getMethodInfo().get(setter).getNames().contains(setter);
        }).collect(Collectors.toList());
  }

  private void rebuildIfNeeded() {
    if (jRocksConfig.isAutoRebuild()) {
      rebuid();
    } else if (scanResult.classpathContentsModifiedSinceScan()) {
      rebuid();
    }
  }
}
