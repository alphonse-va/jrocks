package jrocks.plugin.api.config;

import jrocks.plugin.api.JRocksBean;

import java.util.Optional;

public interface ConfigService extends JRocksBean {

  default String name() {
    return "config";
  }

  void addModule(ModuleConfig module);

  Optional<ModuleConfig> getModule(String type);

  void load();

  void save();

  ProjectConfig getConfig();

  void setConfig(ProjectConfig config);

  boolean isInitialized();
}
