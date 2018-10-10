package jrocks;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ClassPathScanner {

  @Value("${project.base-package}")
  public String proejctBasePackage;

  @Value("${jrocks.classpath.auto-rebuild}")
  private boolean autoRebuild;

  private ClassInfoList classes;

  private ScanResult scanResult;

  @PostConstruct
  public void rebuid() {
    scanResult = new ClassGraph()
        .enableAllInfo()
        .whitelistPackages(proejctBasePackage)
        .scan();
    classes = scanResult.getAllClasses();
  }

  public List<String> getAllCanonicalNames() {
    rebuildIfNeeded();
    return classes.stream().map(ClassInfo::getName).collect(Collectors.toList());
  }

  private void rebuildIfNeeded() {
    if (autoRebuild) {
      rebuid();
    } else if (scanResult.classpathContentsModifiedSinceScan()) {
      rebuid();
    }
  }
}
