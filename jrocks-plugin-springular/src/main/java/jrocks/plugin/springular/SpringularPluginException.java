package jrocks.plugin.springular;

import jrocks.plugin.api.JRocksApiException;

public class SpringularPluginException extends JRocksApiException {

  public SpringularPluginException(String message) {
    super(message);
  }

  public SpringularPluginException(String message, Throwable cause) {
    super(message, cause);
  }

  public SpringularPluginException(Throwable cause) {
    super(cause);
  }

  public SpringularPluginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
