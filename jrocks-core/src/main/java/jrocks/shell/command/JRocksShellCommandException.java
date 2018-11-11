package jrocks.shell.command;

import jrocks.shell.JRocksShellException;

class JRocksShellCommandException extends JRocksShellException {

  JRocksShellCommandException(String message) {
    super(message);
  }

  JRocksShellCommandException(Throwable cause) {
    super(cause);
  }
}
