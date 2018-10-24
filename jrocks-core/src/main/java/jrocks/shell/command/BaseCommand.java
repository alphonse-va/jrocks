package jrocks.shell.command;

import jrocks.shell.TerminalLogger;
import jrocks.shell.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseCommand {

  private ConfigService configService;

  private TerminalLogger logger;

  @Autowired
  protected BaseCommand(ConfigService configService, TerminalLogger logger) {
    this.configService = configService;
    this.logger = logger;
  }

  protected TerminalLogger getLogger() {
    return logger;
  }

  public ConfigService getConfigService() {
    return configService;
  }
}
