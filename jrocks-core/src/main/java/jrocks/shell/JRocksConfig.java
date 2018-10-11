package jrocks.shell;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class JRocksConfig {

  @Value("${jrocks.sourceDirectory}")
  private String sourceDirectory;

  @Value("${project.base-package}")
  public String basePackage;

  @Value("${jrocks.classpath.auto-rebuild}")
  public boolean autoRebuild;

  public String getSourceDirectory() {
    return sourceDirectory;
  }

  public JRocksConfig setSourceDirectory(String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
    return this;
  }

  public String getBasePackage() {
    return basePackage;
  }

  public JRocksConfig setBasePackage(String basePackage) {
    this.basePackage = basePackage;
    return this;
  }

  public boolean isAutoRebuild() {
    return autoRebuild;
  }

  public JRocksConfig setAutoRebuild(boolean autoRebuild) {
    this.autoRebuild = autoRebuild;
    return this;
  }
}
