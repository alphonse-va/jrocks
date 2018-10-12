package jrocks.shell.command;

import ch.qos.logback.classic.Level;
import jrocks.api.ClassInfoApi;
import jrocks.api.ClassInfoParameterApi;
import jrocks.model.BeanClassInfoBuilder;
import jrocks.shell.JRocksConfig;
import jrocks.shell.LogLevel;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import jrocks.shell.autocomplete.AllClassValueProvider;
import jrocks.shell.autocomplete.ClassFieldsValueProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.github.classgraph.utils.ReflectionUtils.classForNameOrNull;
import static java.lang.String.format;
import static java.util.Arrays.asList;

@Component
public abstract class BaseClassInfoCommand {

  private final JRocksConfig jRocksConfig;

  protected BaseClassInfoCommand(final JRocksConfig jRocksConfig) {this.jRocksConfig = jRocksConfig;}

  protected abstract String generateSource(final ClassInfoParameterApi parameter, final ClassInfoApi classInfoApi);

  protected abstract Logger getLogger();

  @ShellMethod(value = "Generate a builder class", key = "builder", group = "builder")
  public void builder(
      @ShellOption(value = "--class", help = "Source class", valueProvider = AllClassValueProvider.class) String classCanonicalName,
      @ShellOption(value = "--force", help = "Overwrite existing files") boolean isForced,
      @ShellOption(value = "--excluded-fields", help = "Fields to exclude", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] excludedFields,
      @ShellOption(value = "--included-fields", help = "Fields to include", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] includedFields,
      @ShellOption(value = "--mandatory-fields", help = "Mandatory fields", defaultValue = "[]", valueProvider = ClassFieldsValueProvider.class) String[] mandatoryFields,
      @ShellOption(value = "--log-level", help = "choice the logs level", defaultValue = "info") LogLevel logLevel
  ) {

    final ClassInfoParameterApi parameter = new BaseClassInfoParameterBuilder()
        .setClassCanonicalName(classCanonicalName)
        .setLogLevel(logLevel)
        .setForce(isForced)
        .setExcludedFields(asList(excludedFields))
        .setIncludedFields(asList(includedFields))
        .setMandatoryFields(asList(mandatoryFields))
        .build();


    setLoggingLevel(logLevel.getLevel());
    // TODO need an aspect to log commands inout params
    getLogger().info("Generate builder for {} class with parameters:\n{}", parameter.getClassCanonicalName(), parameter);

    final String generateSource = callGenerate(parameter);
    writeSource(generateSource, parameter);
  }

  protected String callGenerate(final ClassInfoParameterApi parameter) {
    final ClassInfoApi classInfoApi = prepareParameters(parameter);
    return generateSource(parameter, classInfoApi);
  }

  private ClassInfoApi prepareParameters(final ClassInfoParameterApi parameter) {
    final Class<?> sourceClass = classForNameOrNull(parameter.getClassCanonicalName());
    if (sourceClass == null) {
      throw new IllegalStateException(format("Class '%s' not found on the class path", parameter.getClassCanonicalName()));
    }
    return new BeanClassInfoBuilder<>(sourceClass).build();
  }

  private static void setLoggingLevel(Level level) {
    ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
    root.setLevel(level);
  }

  private void writeSource(String generatedSource, ClassInfoParameterApi parameter) {
    final String relativePath = parameter.getClassCanonicalName().replaceAll("\\.", File.separator);
    final String destPath = format("%s%s%s%s%s.java",
        File.separator,
        jRocksConfig.getSourceDirectory(),
        File.separator,
        relativePath,
        "Builder");

    try {
      final Path path = Paths.get(destPath);
      if (path.toFile().exists() && !parameter.isForce()) {
        getLogger().error("'{}' file exists, please add --force if you want to overwrite", path.toString());
        return;
      }
      final Path savedFile = Files.write(path, generatedSource.getBytes());
      if (parameter.getLogLevel() == LogLevel.debug)
        getLogger().info("'{}' class generated with success.\n\n{}\n", savedFile.getFileName(), generatedSource);
      else
        getLogger().info("'{}' class generated with success.", savedFile.getFileName());
    } catch (IOException e) {
      throw new IllegalStateException(format("Enable to create '%s' file!", destPath), e);
    }
  }
}
