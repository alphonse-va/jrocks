package jrocks.shell.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jrocks.shell.TerminalLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;

import static java.lang.String.format;

@Component
public class ConfigServiceImpl implements ConfigService {

  @Value("${jrocks.configFilename}")
  private String configFileName;

  private ObjectMapper mapper;

  private ProjectConfig config;

  private GlobalConfig globalConfig;

  private TerminalLogger logger;

  @Autowired
  public ConfigServiceImpl(GlobalConfig globalConfig, TerminalLogger logger) {
    this.globalConfig = globalConfig;
    this.logger = logger;
  }

  @PostConstruct
  void postConstruct() {
    mapper = new ObjectMapper(new YAMLFactory());
    try {
      load();
    } catch (RuntimeException e) {
      throw e;
    }
  }

  @Override
  public void addModule(ModuleConfig module) {
    if (config == null) config = new ProjectConfig();
    config.addModule(module);
  }

  @Override
  public void load() {
    try {
      config = mapper.readValue(new File(configFileName), ProjectConfig.class);
    } catch (IOException e) {
      logger.warning("Config file '%s' not found!", configFileName);
      logger.warning("Please initialize your project with 'init' command!");
    }
  }

  @Override
  public void save() {
    if (config == null) throw new IllegalStateException("Config not yet initialized");
    try {
      mapper.writeValue(new File(configFileName), config);
    } catch (IOException e) {
      throw new IllegalStateException(format("Cannot write '%s' YAML file", configFileName), e);
    }
  }

  @Override
  public ProjectConfig getConfig() {
    return config == null ? config = new ProjectConfig() : config;
  }

  @Override
  public void setConfig(ProjectConfig config) {
    this.config = config;
  }

  @Override
  public GlobalConfig getGlobalConfig() {
    return globalConfig;
  }

  @Override
  public boolean isInitialized() {
    return getConfig() != null && !CollectionUtils.isEmpty(getConfig().getModules());
  }
}
