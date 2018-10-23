package jrocks.shell.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.StringJoiner;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix="jrocks")
public class JRocksConfig {

  private String name;

  private String version;

  private String configFileName;

  public String getName() {
    return name;
  }

  public JRocksConfig setName(String name) {
    this.name = name;
    return this;
  }

  public String getVersion() {
    return version;
  }

  public JRocksConfig setVersion(String version) {
    this.version = version;
    return this;
  }

  public String getConfigFileName() {
    return configFileName;
  }

  public JRocksConfig setConfigFileName(String configFileName) {
    this.configFileName = configFileName;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner("\n", "JRocks Configuration:\n", "")
        .add("\tname='" + name + "'")
        .add("\tversion='" + version + "'")
        .add("\tconfigFileName='" + configFileName + "'")
        .toString();
  }
}
