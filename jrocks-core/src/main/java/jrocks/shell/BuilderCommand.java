package jrocks.shell;

import ch.qos.logback.classic.Level;
import jrocks.api.ClassInfoApi;
import jrocks.model.BeanClassInfoBuilder;
import jrocks.shell.valueproviders.AllClassValueProvider;
import jrocks.shell.valueproviders.ClassFieldsValueProvider;
import jrocks.template.bean.builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static io.github.classgraph.utils.ReflectionUtils.classForNameOrNull;
import static java.lang.String.format;

@ShellComponent
public class BuilderCommand {

  private static final Logger LOGGER = LoggerFactory.getLogger(BuilderCommand.class);

  private final JRocksConfig jRocksConfig;

  @Autowired
  public BuilderCommand(JRocksConfig jRocksConfig) {
    this.jRocksConfig = jRocksConfig;
  }

  @ShellMethod(value = "Generate a builder class", key = "builder", group = "builder")
  public void builder(
      @ShellOption(value = "--class", help = "Source class from which you want to generate a builder", valueProvider = AllClassValueProvider.class) String classCanonicalName,
      @ShellOption(value = "--force") boolean force,
      @ShellOption(value = "--excluded-fields", defaultValue = ShellOption.NULL, valueProvider = ClassFieldsValueProvider.class) String[] excludedFields,
      @ShellOption(value = "--log-level", defaultValue = "info") LogLevel logLevel
  ) {

    LOGGER.info("Generate builder for {} class with parameters:\n\tClass: {}\n\tExcluded Fields: {}\n\t", classCanonicalName, classCanonicalName, excludedFields);

    if (logLevel != null) setLoggingLevel(logLevel.getLevel());

    final Class<?> sourceClass = classForNameOrNull(classCanonicalName);
    if (sourceClass == null)
      throw new IllegalStateException(format("Class '%s' not found on the class path", classCanonicalName));

    final ClassInfoApi bean = new BeanClassInfoBuilder<>(sourceClass).build();
    String generatedSource = builder.template(bean).render().toString();
    writeGeneratedFile(bean.canonicalName().replaceAll("\\.", File.separator), generatedSource, force, sourceClass, logLevel);
  }

  private static void setLoggingLevel(Level level) {
    ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger) org.slf4j.LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
    root.setLevel(level);
  }

  private void writeGeneratedFile(String relativePath, String generatedSource, boolean force, Class<?> clazz, final LogLevel logLevel) {
    final String destPath = format("%s%s%s%s%s.java",
        File.separator,
        jRocksConfig.getSourceDirectory(),
        File.separator,
        relativePath,
        "Builder");

    try {
      final Path path = Paths.get(destPath);
      if (path.toFile().exists() && !force) {
        LOGGER.error("'{}' file exists, please use --force if you want to overwrite", path.toString());
        return;
      }
      final Path savedFile = Files.write(path, generatedSource.getBytes());
      if (logLevel == LogLevel.debug)
        LOGGER.info("'{}' class generated with success.\n\nSource: {}\nGenerated: {}\n\n{}\n", savedFile.getFileName(), clazz + ".java", savedFile, generatedSource);
      else
        LOGGER.info("'{}' class generated with success.\n\nSource: {}\nGenerated: {}", savedFile.getFileName(), clazz + ".java", savedFile);
    } catch (IOException e) {
      throw new IllegalStateException(format("Enable to create '%s' file!", destPath), e);
    }

  }
}
