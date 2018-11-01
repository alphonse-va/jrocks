package jrocks.shell.config;

public interface ConfigService {
  void addModule(ModuleConfig module);

  void load();

  void save();

  ProjectConfig getConfig();

  void setConfig(ProjectConfig config);

  GlobalConfig globalConfig();

  boolean isInitialized();
}
