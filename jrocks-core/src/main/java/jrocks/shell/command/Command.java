package jrocks.shell.command;

import jrocks.shell.TerminalLogger;
import jrocks.shell.config.ConfigService;

import java.util.ArrayList;
import java.util.List;

public interface Command {

  TerminalLogger terminalLogger();

  ConfigService configService();

  default List<String> additionalFlags() {
    return new ArrayList<>();
  }
}
