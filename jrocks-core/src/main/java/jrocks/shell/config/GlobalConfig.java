package jrocks.shell.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.StringJoiner;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="jrocks")
public class GlobalConfig {

  private String name;

  private String version;

  private String configFileName;

  private File pluginDirectory;

  public String getName() {
    return name;
  }

  public GlobalConfig setName(String name) {
    this.name = name;
    return this;
  }

  public String getVersion() {
    return version;
  }

  public GlobalConfig setVersion(String version) {
    this.version = version;
    return this;
  }

  public String getConfigFileName() {
    return configFileName;
  }

  public GlobalConfig setConfigFileName(String configFileName) {
    this.configFileName = configFileName;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner("\n", "JRocks Configuration:\n", "")
        .add("\tName:'" + name + "'")
        .add("\tVersion: '" + version + "'")
        .add("\tConfig Filename: '" + configFileName + "'")
        .toString();
  }

  public File getPluginDirectory() {
    return pluginDirectory;
  }

  public GlobalConfig setPluginDirectory(File pluginDirectory) {
    this.pluginDirectory = pluginDirectory;
    return this;
  }
}
