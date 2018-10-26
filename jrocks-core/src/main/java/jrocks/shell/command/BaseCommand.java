package jrocks.shell.command;

import jrocks.shell.TerminalLogger;
import jrocks.shell.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseCommand implements Command {

  private ConfigService configService;

  private TerminalLogger logger;

  @Autowired
  protected BaseCommand(ConfigService configService, TerminalLogger logger) {
    this.configService = configService;
    this.logger = logger;
  }

  @Override
  public TerminalLogger terminalLogger() {
    return logger;
  }

  @Override
  public ConfigService configService() {
    return configService;
  }
}
