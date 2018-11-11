package jrocks.shell.config;

import javax.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.StringJoiner;

public class ModuleConfig {

  @NotBlank
  private String name;

  private String version;

  @NotBlank
  private String sourceDirectory;

  @NotBlank
  private String outputDirectory;

  public String getName() {
    return name;
  }

  public ModuleConfig setName(String name) {
    this.name = name;
    return this;
  }

  public String getVersion() {
    return version;
  }

  public ModuleConfig setVersion(String version) {
    this.version = version;
    return this;
  }

  public String getSourceDirectory() {
    return sourceDirectory;
  }

  public ModuleConfig setSourceDirectory(String sourceDirectory) {
    this.sourceDirectory = sourceDirectory;
    return this;
  }

  public String getOutputDirectory() {
    return outputDirectory;
  }

  public ModuleConfig setOutputDirectory(String outputDirectory) {
    this.outputDirectory = outputDirectory;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ModuleConfig that = (ModuleConfig) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(version, that.version) &&
        Objects.equals(sourceDirectory, that.sourceDirectory) &&
        Objects.equals(outputDirectory, that.outputDirectory);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name, version, sourceDirectory, outputDirectory);
  }

  @Override
  public String toString() {
    return new StringJoiner("\n", "\n", "\n")
        .add("\t\tName: _" + name + "_")
        .add("\t\tVersion: _" + version + "_")
        .add("\t\tSource Directory: _" + sourceDirectory + "_")
        .add("\t\tOutput Directory: _" + outputDirectory + "_")
        .toString();
  }
}
