package jrocks.shell.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import jrocks.plugin.api.config.ConfigService;
import jrocks.plugin.api.config.ModuleConfig;
import jrocks.plugin.api.config.ProjectConfig;
import jrocks.shell.JRocksShellException;
import jrocks.plugin.api.shell.TerminalLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

import static java.lang.String.format;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

@Component
public class BaseConfigService implements ConfigService {

  @Value("${jrocks.configFilename}")
  private String configFileName;

  private ObjectMapper mapper;

  private ProjectConfig config;

  private TerminalLogger terminalLogger;

  @Autowired
  public BaseConfigService(TerminalLogger terminalLogger) {
    this.terminalLogger = terminalLogger;
  }

  @PostConstruct
  void postConstruct() {
    mapper = new ObjectMapper(new YAMLFactory());
    load();
  }

  @Override
  public void addModule(ModuleConfig module) {
    if (config == null) config = new ProjectConfig();
    config.addModule(module);
  }

  @Override
  public Optional<ModuleConfig> getModule(String type) {
    return config.getModules().stream()
        .filter(m -> m.getTypes().stream().anyMatch( t -> t.name().equals(type)))
        .findAny();
  }


  @Override
  public void load() {
    try {
      config = mapper.readValue(new File(configFileName), ProjectConfig.class);
    } catch (IOException e) {
      e.printStackTrace();
      terminalLogger.error(this, "Error while reading '%s' file. Error: %s", configFileName, e.getMessage());
      terminalLogger.warning(this, "Please fix your project config!");
    }
  }

  @Override
  public void save() {
    Assert.notNull(config, "Configuration not yet initialized!");
    try {
      mapper.writeValue(new File(configFileName), config);
    } catch (IOException e) {
      throw new JRocksShellException(format("Cannot write '%s' YAML file", configFileName), e);
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
  public boolean isInitialized() {
    return getConfig() != null && isNotEmpty(getConfig().getModules());
  }
}
