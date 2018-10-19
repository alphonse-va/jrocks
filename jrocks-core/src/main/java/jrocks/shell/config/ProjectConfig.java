package jrocks.shell.config;

public class ProjectConfig {

  private String name;
  private String version;
  private String sourceDirectory;
  private String outputDirectory;
  private String buildDirectory;
  private String basePackage;

  public String getName() {
    return name;
  }

  public ProjectConfig setName(String name) {
    this.name = name;
    return this;
  }

  public String getVersion() {
    return version;
  }

  public ProjectConfig setVersion(String version) {
    this.version = version;
    return this;
  }

  public String getSourceDirectory() {
    return sourceDirectory;
  }

  public ProjectConfig setSourceDirectory(String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
    return this;
  }

  public String getOutputDirectory() {
    return outputDirectory;
  }

  public ProjectConfig setOutputDirectory(String outputDirectory) {
    this.outputDirectory = outputDirectory;
    return this;
  }

  public String getBuildDirectory() {
    return buildDirectory;
  }

  public ProjectConfig setBuildDirectory(String buildDirectory) {
    this.buildDirectory = buildDirectory;
    return this;
  }

  public String getBasePackage() {
    return basePackage;
  }

  public ProjectConfig setBasePackage(String basePackage) {
    this.basePackage = basePackage;
    return this;
  }
}
