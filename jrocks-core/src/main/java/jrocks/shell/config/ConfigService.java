package jrocks.shell.config;

import jrocks.plugin.api.JRocksBean;

public interface ConfigService extends JRocksBean {

  default String name() {
    return "config";
  }

  void addModule(ModuleConfig module);

  void load();

  void save();

  ProjectConfig getConfig();

  void setConfig(ProjectConfig config);

  GlobalConfig globalConfig();

  boolean isInitialized();
}
