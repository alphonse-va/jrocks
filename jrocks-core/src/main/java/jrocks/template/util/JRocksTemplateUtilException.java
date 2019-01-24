package jrocks.template.util;

import jrocks.plugin.api.JRocksApiException;

import java.io.IOException;

class JRocksTemplateUtilException extends JRocksApiException {

  JRocksTemplateUtilException(String message) {
    super(message);
  }

  JRocksTemplateUtilException(Throwable cause) {
    super(cause);
  }

  public JRocksTemplateUtilException(String message, IOException cause) {
    super(message, cause);
  }
}
