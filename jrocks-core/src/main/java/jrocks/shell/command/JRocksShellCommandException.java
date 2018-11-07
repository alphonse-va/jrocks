package jrocks.shell.command;

import jrocks.shell.JRocksShellException;

public class JRocksShellCommandException extends JRocksShellException {

  public JRocksShellCommandException(String message) {
    super(message);
  }

  public JRocksShellCommandException(String message, Throwable cause) {
    super(message, cause);
  }

  public JRocksShellCommandException(Throwable cause) {
    super(cause);
  }

  public JRocksShellCommandException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
