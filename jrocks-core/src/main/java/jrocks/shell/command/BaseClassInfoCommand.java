package jrocks.shell.command;

import ch.qos.logback.classic.Level;
import jrocks.api.ClassInfoApi;
import jrocks.api.ClassInfoParameterApi;
import jrocks.model.BaseClassInfoBuilder;
import jrocks.shell.JRocksConfig;
import jrocks.shell.TerminalLogger;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME;
import static io.github.classgraph.utils.ReflectionUtils.classForNameOrNull;
import static java.lang.String.format;

public abstract class BaseClassInfoCommand {

  private final JRocksConfig jRocksConfig;

  private TerminalLogger terminalLogger;

  protected BaseClassInfoCommand(final JRocksConfig jRocksConfig, TerminalLogger terminalLogger) {
    this.terminalLogger = terminalLogger;
    this.jRocksConfig = jRocksConfig;
  }

  protected ClassInfoApi getClassInfoApi(final ClassInfoParameterApi parameter) {
    final Class<?> sourceClass = classForNameOrNull(parameter.getClassCanonicalName());
    if (sourceClass == null) {
      throw new IllegalStateException(format("Class '%s' not found on the class path", parameter.getClassCanonicalName()));
    }
    return new BaseClassInfoBuilder(sourceClass).build();
  }

  protected static void setLoggingLevel(Level level) {
    ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ROOT_LOGGER_NAME);
    root.setLevel(level);
  }

  protected void writeSource(String generatedSource, ClassInfoParameterApi parameter) {
    final String sourceName = StringUtils.substringAfterLast(parameter.getClassCanonicalName(), ".");
    final String relativePath = StringUtils.removeEnd(parameter.getClassCanonicalName(), sourceName)
        .replaceAll("\\.", File.separator);

    final String destDir = format("%s%s%s%s",
        File.separator,
        jRocksConfig.getSourceDirectory(),
        File.separator,
        relativePath
    );

    final Path path = Paths.get(destDir + File.separator + sourceName + parameter.suffix() + ".java");
    final File file = path.toFile();
    if (file.exists() && !parameter.isForce()) {
      terminalLogger.error("%s file exists, please add --force if you want to overwrite", path.toString());
      return;
    }
    try {
      final File dir = new File(destDir);
      if (!dir.isDirectory()) {
        boolean success = dir.mkdirs();
        if (success) {
          terminalLogger.info("Created path: " + dir.getPath());
        } else {
          terminalLogger.info("Could not create path: " + dir.getPath());
        }
      } else {
        terminalLogger.info("Path exists: " + dir.getPath());
      }
      final Path savedFile = Files.write(path, generatedSource.getBytes());
      terminalLogger.info("%s class generated with success.", savedFile.getFileName());
      terminalLogger.verbose("Generated source: \n\n%s", generatedSource);
    } catch (IOException e) {
      throw new IllegalStateException(format("Enable to create '%s' file!", destDir), e);
    }
  }

  protected TerminalLogger getLogger() {
    return terminalLogger;
  }
}