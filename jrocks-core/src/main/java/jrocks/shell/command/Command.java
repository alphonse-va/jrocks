package jrocks.shell.command;

import jrocks.plugin.api.config.ConfigService;
import jrocks.plugin.api.shell.TerminalLogger;

public interface Command {

  TerminalLogger terminalLogger();

  ConfigService configService();
}
