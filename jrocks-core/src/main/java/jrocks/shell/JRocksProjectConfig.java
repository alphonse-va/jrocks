package jrocks.shell;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.util.Properties;
import java.util.StringJoiner;

import static java.lang.String.format;
import static jrocks.shell.JRocksProjectConfig.ProjectProperty.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class JRocksProjectConfig {

  @Value("${jrocks.configFileName}")
  private String configFileName;

  private boolean initialized = false;

  private JRocksProperties properties = new JRocksProperties();

  private String sourceDirectory = "src/main/java";
  private String outputDirectory = "target/classes";
  private String buildDirectory = "target";
  private String basePackage = "";
  private boolean autoRebuild = true;

  public enum ProjectProperty {
    SOURCE_DIRECTORY("sourceDirectory"),
    OUTPUT_DIRECTORY("outputDirectory"),
    BUILD_DIRECTORY("buildDirectory"),
    BASE_PACKAGE("basePackage"),
    AUTO_REBUILD("autoRebuild");

    private final String propertyName;

    ProjectProperty(String propertyName) {
      this.propertyName = propertyName;
    }

    public String getPropertyName() {
      return propertyName;
    }
  }

  @PostConstruct
  void postConstruct() {
    try {
      reload();
      initialized = true;
    } catch (RuntimeException e) {
      initialized = false;
    }
  }

  private void reload() {
    try (InputStream inputStream = new FileInputStream(configFileName)) {
      properties.load(inputStream);
    } catch (IOException e) {
      throw new IllegalStateException("JRocks config file not found", e);
    }

    sourceDirectory = properties.getString(SOURCE_DIRECTORY);
    outputDirectory = properties.getString(OUTPUT_DIRECTORY);
    buildDirectory = properties.getString(BUILD_DIRECTORY);
    basePackage = properties.getString(BASE_PACKAGE);
    autoRebuild = properties.getBoolean(AUTO_REBUILD);
    initialized = true;
  }

  public void store() {
    try (Writer propertiesFileWriter = new FileWriter(configFileName)) {
      properties.store(propertiesFileWriter, "JRocks configuration file");
    } catch (IOException e) {
      throw new IllegalStateException(format("Error while writing '%s' config file", configFileName), e);
    }
    reload();
  }

  public void storeProperty(ProjectProperty property, String value) {
    properties.put(property, value);
    store();
  }

  public boolean isInitialized() {
    return initialized
        && isNotBlank(sourceDirectory)
        && isNotBlank(outputDirectory)
        && isNotBlank(buildDirectory)
        && isNotBlank(basePackage);
  }

  public String getSourceDirectory() {
    return sourceDirectory;
  }

  public void setSourceDirectory(String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
    properties.put(SOURCE_DIRECTORY, sourceDirectory);
  }

  public String getOutputDirectory() {
    return outputDirectory;
  }

  public void setOutputDirectory(String outputDirectory) {
    this.outputDirectory = outputDirectory;
    properties.put(OUTPUT_DIRECTORY, outputDirectory);
  }

  public String getBuildDirectory() {
    return buildDirectory;
  }

  public void setBuildDirectory(String buildDirectory) {
    this.buildDirectory = buildDirectory;
    properties.put(BUILD_DIRECTORY, buildDirectory);
  }

  public String getBasePackage() {
    return basePackage;
  }

  public void setBasePackage(String basePackage) {
    this.basePackage = basePackage;
    properties.put(BASE_PACKAGE, basePackage);
  }

  public boolean isAutoRebuild() {
    return autoRebuild;
  }

  public void setAutoRebuild(boolean autoRebuild) {
    this.autoRebuild = autoRebuild;
    properties.put(AUTO_REBUILD, autoRebuild ? "true" : "false");
  }

  private static class JRocksProperties extends Properties {

    void put(ProjectProperty key, String value) {
      put(key.getPropertyName(), value);
    }

    String getString(ProjectProperty key) {
      return getProperty(key.getPropertyName());
    }

    boolean getBoolean(ProjectProperty key) {
      return parseBoolean(getProperty(key.getPropertyName()));
    }

    @Override
    public String getProperty(String key, String def) {
      throw new IllegalArgumentException("not supported");
    }

    private boolean parseBoolean(String value) {
      switch (value) {
        case "true":
        case "1":
        case "on":
          return true;
        default:
          return false;
      }
    }
  }

  @Override
  public String toString() {
    return new StringJoiner("\n", "Project Configuration:\n\n", "")
        .add("\tsourceDirectory='" + sourceDirectory + "'")
        .add("\toutputDirectory='" + outputDirectory + "'")
        .add("\tbuildDirectory='" + buildDirectory + "'")
        .add("\tbasePackage='" + basePackage + "'")
        .add("\tautoRebuild=" + autoRebuild)
        .toString();
  }
}
