package jrocks.shell.command;

import jrocks.shell.TerminalLogger;
import jrocks.shell.config.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

/**
 *
 * Base command
 *
 * NOTE:   we use setter injection to prevent circular dependencies in plugin commands
 */
public abstract class BaseCommand implements Command {

  private ConfigService configService;

  private TerminalLogger logger;

  @Override
  public TerminalLogger terminalLogger() {
    return logger;
  }

  @Override
  public ConfigService configService() {
    return configService;
  }

  @Lazy
  @Autowired
  public void setConfigService(ConfigService configService) {
    this.configService = configService;
  }

  @Lazy
  @Autowired
  public void setLogger(TerminalLogger logger) {
    this.logger = logger;
  }
}
