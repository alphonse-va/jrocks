package jrocks.shell.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jrocks.shell.JRocksShellException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class ProjectConfig {

  private String basePackage = "";
  private boolean autoRebuild = true;
  private Set<ModuleConfig> modules;

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
        throw new JRocksShellException("Not valid URL " + m.getOutputDirectory());
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
        .add("\tBase Package: '" + basePackage + "'")
        .add("\tAuto Rebuild: " + autoRebuild)
        .add("\tModules:" + modules.stream().map(ModuleConfig::toString).collect(Collectors.joining()))
        .toString();
  }
}
