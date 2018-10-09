package jrocks.shell;

import java.io.*;
import java.util.Optional;
import java.util.Properties;

public class JRocksConfigHolder {

  private static final String CONFIG_FILE_NAME = "/.jrocks";
  public static final String PROJECT_BASE_DIRECTORY = System.getProperty("user.dir");

  private static Properties properties = new Properties();
  private static File file;

  private static  boolean initialied;

  public enum JRocksConfig {
    PROJECT_BASE_PACKAGE("jrocks.package"),
    TARGET_SOURCE_DIRECTORY("jrocks.source.directory"),
    TARGET_CLASS_DIRECTORY("jrocks.class.directory");

    private String key;

    JRocksConfig(String key) {
      this.key = key;
    }

    public String getKey() {
      return key;
    }
  }

  private static void load() {
    try {
      InputStream is = new FileInputStream(getConfigFile());
      properties.load(is);
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage() + "\n\n Please initialize your project with 'jrocks load [param]' command", e);
    }
  }

  public static boolean check() {
    load();
//    Stream.of(JRocksConfig.values())
//        .forEach(c -> {
//          if (!getConfig(c).isPresent())
//            throw new IllegalStateException(format("%s configuration property no found in %s file.", c.getKey(), getConfigFile().getAbsolutePath()));
//        });
    initialied = true;
    return initialied;
  }

  public static Optional<String> getConfig(JRocksConfig config) {
    if (!initialied) {
      check();
      initialied = true;
    }
    return Optional.ofNullable(properties.getProperty(config.getKey()));
  }

  public static void setConfig(JRocksConfig config, String value) {
    properties.setProperty(config.getKey(), value);
    try {
      properties.store(new FileOutputStream(getConfigFile()), null);
    } catch (IOException e) {
      throw new IllegalStateException(e.getLocalizedMessage(), e);
    }
  }

  public static boolean exists() {
    return getConfigFile().exists();
  }

  private static File getConfigFile() {
    if (file == null) file = new File(PROJECT_BASE_DIRECTORY + CONFIG_FILE_NAME);
    return file;
  }
}
