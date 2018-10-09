package jrocks.shell;

import ch.qos.logback.classic.Level;

public enum LogLevel {

  debug(Level.DEBUG),
  info(Level.INFO),
  warn(Level.WARN),
  error(Level.ERROR),
  all(Level.ALL);

  private final Level level;

  LogLevel(Level level) {
    this.level = level;
  }

  public Level getLevel() {
    return level;
  }
}
