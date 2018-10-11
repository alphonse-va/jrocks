package jrocks;

import java.util.Objects;

/**
 * Builder for the JRocksApplication bean.
 */
public class JRocksApplicationBuilder {

  private JRocksApplication jRocksApplication = new JRocksApplication();

  public JRocksApplication build() {
    return jRocksApplication;
  }

}