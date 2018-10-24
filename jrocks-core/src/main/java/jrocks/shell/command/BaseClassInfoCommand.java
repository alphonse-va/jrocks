package jrocks.shell.command;

import ch.qos.logback.classic.Level;
import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoParameter;
import jrocks.model.ClassInfoBuilder;
import jrocks.shell.ClassPathScanner;
import jrocks.shell.TerminalLogger;
import jrocks.shell.config.ConfigService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME;
import static java.lang.String.format;

public abstract class BaseClassInfoCommand extends BaseCommand {

  @Autowired
  private ClassPathScanner classPathScanner;

  protected BaseClassInfoCommand(ConfigService configService, TerminalLogger terminalLogger) {
    super(configService, terminalLogger);
  }

  ClassInfo getClassInfo(ClassInfoParameter parameter) {
    io.github.classgraph.ClassInfo sourceClass = classPathScanner.getAllClassInfo()
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
