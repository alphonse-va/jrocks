package jrocks;

public class JRocksBaseException extends RuntimeException {

  public JRocksBaseException(String message) {
    super(message);
  }

  public JRocksBaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public JRocksBaseException(Throwable cause) {
    super(cause);
  }

  public JRocksBaseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
