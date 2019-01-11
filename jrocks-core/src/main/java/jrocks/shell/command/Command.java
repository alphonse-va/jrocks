package jrocks.shell.command;

import jrocks.plugin.api.config.ConfigService;
import jrocks.shell.TerminalLogger;

public interface Command {

  TerminalLogger terminalLogger();

  ConfigService configService();
}
