package jrocks.plugin.api;

public class JRocksApiException extends RuntimeException {

  public JRocksApiException(String message) {
    super(message);
  }

  public JRocksApiException(String message, Throwable cause) {
    super(message, cause);
  }

  public JRocksApiException(Throwable cause) {
    super(cause);
  }

  public JRocksApiException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
