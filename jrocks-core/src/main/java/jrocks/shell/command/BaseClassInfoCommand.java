package jrocks.shell.command;

import ch.qos.logback.classic.Level;
import io.github.classgraph.ClassInfo;
import jrocks.api.ClassInfoApi;
import jrocks.api.ClassInfoParameterApi;
import jrocks.model.ClassInfoBuilder;
import jrocks.shell.ClassPathScanner;
import jrocks.shell.TerminalLogger;
import jrocks.shell.config.JRocksConfig;
import jrocks.shell.config.JRocksProjectConfig;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME;
import static java.lang.String.format;

public abstract class BaseClassInfoCommand extends BaseCommand {

  @Autowired
  private ClassPathScanner classPathScanner;

  protected BaseClassInfoCommand(JRocksConfig jRocksConfig, JRocksProjectConfig projectConfig, TerminalLogger terminalLogger) {
    super(jRocksConfig, projectConfig, terminalLogger);
  }

  ClassInfoApi getClassInfoApi(ClassInfoParameterApi parameter) {
    ClassInfo sourceClass = classPathScanner.getAllClassInfo()
        .filter(ci -> ci.getName().equals(parameter.getClassCanonicalName()))
        .findAny()
        .orElseThrow(() -> new IllegalStateException(format("Class '%s' not found on the class path", parameter.getClassCanonicalName())));
    return new ClassInfoBuilder(sourceClass).build();
  }

  protected static void setLoggingLevel(Level level) {
    ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);
    root.setLevel(level);
  }
}
