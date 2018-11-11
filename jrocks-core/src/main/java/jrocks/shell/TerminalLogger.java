package jrocks.shell;

import jrocks.plugin.api.JRocksPlugin;

public interface TerminalLogger {

  void info(String message, Object... values);

  void warning(String message, Object... values);

  void error(String message, Object... values);

  void verbose(String message, Object... values);

  void info(JRocksPlugin plugin, String message, Object... values);

  void warning(JRocksPlugin plugin, String message, Object... values);

  void error(JRocksPlugin plugin, String message, Object... values);

  void verbose(JRocksPlugin plugin, String message, Object... values);

  void setMessagePrefix(String messagePrefix);

  void setDefaultMessagePrefix();

  void setVerbose(boolean verbose);

  boolean isVerbose();

  void newline();
}
