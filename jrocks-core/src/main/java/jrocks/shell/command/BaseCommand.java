package jrocks.shell.command;

import jrocks.shell.config.JRocksConfig;
import jrocks.shell.config.JRocksProjectConfig;
import jrocks.shell.TerminalLogger;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseCommand {

  private final JRocksConfig config;

  private JRocksProjectConfig projectConfig;

  private TerminalLogger logger;

  @Autowired
  protected BaseCommand(JRocksConfig config, JRocksProjectConfig projectConfig, TerminalLogger logger) {
    this.projectConfig = projectConfig;
    this.logger = logger;
    this.config = config;
  }


  protected TerminalLogger getLogger() {
    return logger;
  }

  protected JRocksConfig getConfig() {
    return config;
  }

  public JRocksProjectConfig getProjectConfig() {
    return projectConfig;
  }
}
