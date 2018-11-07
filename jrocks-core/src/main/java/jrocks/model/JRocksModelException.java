package jrocks.model;

import jrocks.JRocksBaseException;

public class JRocksModelException extends JRocksBaseException {

  public JRocksModelException(String message) {
    super(message);
  }

  public JRocksModelException(String message, Throwable cause) {
    super(message, cause);
  }

  public JRocksModelException(Throwable cause) {
    super(cause);
  }

  public JRocksModelException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
