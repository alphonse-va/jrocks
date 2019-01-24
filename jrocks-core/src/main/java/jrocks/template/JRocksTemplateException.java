package jrocks.template;

import jrocks.plugin.api.JRocksApiException;

import java.io.IOException;

class JRocksTemplateException extends JRocksApiException {

  JRocksTemplateException(String message) {
    super(message);
  }

  JRocksTemplateException(Throwable cause) {
    super(cause);
  }

  public JRocksTemplateException(String message, IOException cause) {
    super(message, cause);
  }
}
