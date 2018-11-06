package jrocks.mock;

import jrocks.shell.TerminalLogger;

public class TerminalLoggerMock implements TerminalLogger {

  @Override
  public void info(String message, Object... values) {

  }

  @Override
  public void warning(String message, Object... values) {

  }

  @Override
  public void error(String message, Object... values) {

  }

  @Override
  public void verbose(String message, Object... values) {

  }

  @Override
  public void setMessagePrefix(String messagePrefix) {

  }

  @Override
  public void setDefaultMessagePrefix() {

  }

  @Override
  public void setVerbose(boolean verbose) {

  }

  @Override
  public boolean isVerbose() {
    return false;
  }

  @Override
  public void newline() {

  }
}
