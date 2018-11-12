package jrocks.shell;

import jrocks.plugin.api.JRocksBean;

public interface TerminalLogger {

  void info(String message, Object... values);

  void warning(String message, Object... values);

  void error(String message, Object... values);

  void verbose(String message, Object... values);

  void info(JRocksBean jrBean, String message, Object... values);

  void warning(JRocksBean jrBean, String message, Object... values);

  void error(JRocksBean jrBean, String message, Object... values);

  void verbose(JRocksBean jrBean, String message, Object... values);

  void setMessagePrefix(String messagePrefix);

  void setDefaultMessagePrefix();

  void setVerbose(boolean verbose);

  boolean isVerbose();

  void newline();
}
