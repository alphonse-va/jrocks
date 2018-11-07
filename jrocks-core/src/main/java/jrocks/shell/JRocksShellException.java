package jrocks.shell;

import jrocks.JRocksBaseException;

public class JRocksShellException extends JRocksBaseException {

  public JRocksShellException(String message) {
    super(message);
  }

  public JRocksShellException(String message, Throwable cause) {
    super(message, cause);
  }

  public JRocksShellException(Throwable cause) {
    super(cause);
  }

  public JRocksShellException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
