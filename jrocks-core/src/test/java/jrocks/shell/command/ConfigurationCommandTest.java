package jrocks.shell.command;

import jrocks.shell.config.MavenProjectUtil;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConfigurationCommandTest {

  private static ConfigurationCommand config;

  @BeforeAll
  private static void beforeAll() {
    config = new ConfigurationCommand(new MavenProjectUtil());
  }

  @Test
  void pluginsList() {
    config.plugins(true);
  }
}