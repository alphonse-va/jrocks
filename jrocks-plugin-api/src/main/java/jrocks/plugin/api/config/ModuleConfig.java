package jrocks.plugin.api.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.StringJoiner;

public class ModuleConfig {

  private String name;
  private String version;
  private String sourceDirectory;
  private String outputDirectory;
  private String baseDirectory;
  private List<ModuleType> types;

  public void addType(ModuleType type) {
    if (types == null) types = new ArrayList<>();
    this.types.add(type);
  }

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

  public List<ModuleType> getTypes() {
    return types;
  }

  public void setTypes(List<ModuleType> types) {
    this.types = types;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    ModuleConfig that = (ModuleConfig) o;
    return Objects.equals(name, that.name) &&
        Objects.equals(version, that.version) &&
        Objects.equals(sourceDirectory, that.sourceDirectory) &&
        Objects.equals(outputDirectory, that.outputDirectory) &&
        Objects.equals(types, that.types);
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
        .add("\t\tType: _" + types + "_")
        .toString();
  }

  public String getBaseDirectory() {
    return baseDirectory;
  }

  public void setBaseDirectory(String baseDirectory) {
    this.baseDirectory = baseDirectory;
  }
}
