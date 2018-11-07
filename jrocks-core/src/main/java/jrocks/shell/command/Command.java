package jrocks.shell.command;

import jrocks.shell.TerminalLogger;
import jrocks.shell.config.ConfigService;

public interface Command {

  TerminalLogger terminalLogger();

  ConfigService configService();
}
