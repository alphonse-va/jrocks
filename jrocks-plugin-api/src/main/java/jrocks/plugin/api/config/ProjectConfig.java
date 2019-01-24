package jrocks.plugin.api.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jrocks.plugin.api.JRocksApiException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class ProjectConfig {

  private String basePackage = "";
  private boolean autoRebuild = true;
  private Set<ModuleConfig> modules;

  public Optional<ModuleConfig> findModuleByType(ModuleType type) {
    return modules.stream().filter(m -> m.getTypes().contains(type)).findAny();
  }

  public void addModule(ModuleConfig module) {
    if (modules == null) modules = new HashSet<>();
    modules.add(module);
  }

  @JsonIgnore // @YalmIgnore
  public URL[] getOutputDirectoriesAsURLs() {
    return getModules().stream().map(m -> {
      try {
        return new URL("file://" + m.getOutputDirectory());
      } catch (MalformedURLException e) {
        throw new JRocksApiException("Not valid URL " + m.getOutputDirectory(), e);
      }
    }).toArray(URL[]::new);
  }

  public Set<ModuleConfig> getModules() {
    return modules == null ? modules = new HashSet<>() : modules;
  }

  public String getBasePackage() {
    return basePackage;
  }

  public ProjectConfig setBasePackage(String basePackage) {
    this.basePackage = basePackage;
    return this;
  }

  public boolean isAutoRebuild() {
    return autoRebuild;
  }

  public ProjectConfig setAutoRebuild(boolean autoRebuild) {
    this.autoRebuild = autoRebuild;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner("\n", "Project Configuration:\n", "")
        .add("\tBase Package: _" + basePackage + "_")
        .add("\tAuto Rebuild: _" + autoRebuild + "_")
        .add("\tModules: " + modules.stream().map(ModuleConfig::toString).collect(Collectors.joining(" ", "_", "_")))
        .toString();
  }
}
