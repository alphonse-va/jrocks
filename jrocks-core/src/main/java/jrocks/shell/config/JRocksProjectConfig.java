package jrocks.shell.config;

import com.google.common.annotations.VisibleForTesting;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static jrocks.shell.config.JRocksProjectConfig.PropertyCode.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Component
public class JRocksProjectConfig {

  private static final String FILE_URL_PREFIX = "file://";

  @Value("${jrocks.configFileName}")
  private String configFileName;

  private boolean initialized = false;

  private JRocksProperties properties = new JRocksProperties();

  private List<String> sourceDirectories = new ArrayList<>();
  private List<String> outputDirectories = new ArrayList<>();
  private List<String> buildDirectories = new ArrayList<>();
  private String basePackage = "";
  private boolean autoRebuild = true;

  public enum PropertyCode {
    SOURCE_DIRECTORIES("sourceDirectories"),
    OUTPUT_DIRECTORIES("outputDirectories"),
    BUILD_DIRECTORIES("buildDirectories"),
    BASE_PACKAGE("basePackage"),
    AUTO_REBUILD("autoRebuild");

    private final String propertyName;

    PropertyCode(String propertyName) {
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

    sourceDirectories = properties.getList(SOURCE_DIRECTORIES);
    outputDirectories = properties.getList(OUTPUT_DIRECTORIES);
    buildDirectories = properties.getList(BUILD_DIRECTORIES);
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

  public void clearDirectories() {
    sourceDirectories = new ArrayList<>();
    outputDirectories = new ArrayList<>();
    buildDirectories = new ArrayList<>();
  }

  public void storeProperty(PropertyCode property, String value) {
    properties.put(property, value);
    store();
  }

  public boolean isInitialized() {
    return initialized
        && sourceDirectories.stream().anyMatch(StringUtils::isNotBlank)
        && outputDirectories.stream().anyMatch(StringUtils::isNotBlank)
        && buildDirectories.stream().anyMatch(StringUtils::isNotBlank)
        && isNotBlank(basePackage);
  }

  public List<URL> getOutputDirectoriesAsURLs() {
    return outputDirectories.stream().map(dir -> {
      try {
        return new URL(FILE_URL_PREFIX + dir);
      } catch (MalformedURLException e) {
        throw new IllegalStateException("Output directory '" + dir + "' is not a valid url", e);
      }
    }).collect(Collectors.toList());
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

  public List<String> getSourceDirectories() {
    return sourceDirectories;
  }

  public void addSourceDirectory(String sourceDirectory) {
    sourceDirectories.add(sourceDirectory);
    properties.put(SOURCE_DIRECTORIES, String.join(",", sourceDirectories));
  }

  public List<String> getOutputDirectories() {
    return outputDirectories;
  }

  public void addOutputDirectory(String outputDirectoy) {
    outputDirectories.add(outputDirectoy);
    properties.put(OUTPUT_DIRECTORIES, String.join(",", outputDirectories));
  }

  public List<String> getBuildDirectoryFor(String source) {
    return buildDirectories;
  }

  public void addBuildDirectory(String buildDirectory) {
    buildDirectories.add(buildDirectory);
    properties.put(BUILD_DIRECTORIES, String.join(",", outputDirectories));
  }

  private static class JRocksProperties extends Properties {

    static final String LIST_SEPARATOR_REGEXP = ",";

    void put(PropertyCode key, String value) {
      put(key.getPropertyName(), value);
    }

    String getString(PropertyCode key) {
      return getProperty(key.getPropertyName());
    }

    boolean getBoolean(PropertyCode key) {
      return parseBoolean(getProperty(key.getPropertyName()));
    }

    List<String> getList(PropertyCode key) {
      String listAsString = getString(key);
      if (isBlank(listAsString)) {
        return new ArrayList<>();
      }
      return new ArrayList<>(asList(listAsString.split(LIST_SEPARATOR_REGEXP)));
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

  @VisibleForTesting
  public JRocksProjectConfig setInitialized(boolean initialized) {
    this.initialized = initialized;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner("\n", "Project Configuration:\n\n", "")
        .add("\tsourceDirectories='" + sourceDirectories + "'")
        .add("\toutputDirectories='" + outputDirectories + "'")
        .add("\tbuildDirectories='" + buildDirectories + "'")
        .add("\tbasePackage='" + basePackage + "'")
        .add("\tautoRebuild=" + autoRebuild)
        .toString();
  }
}
