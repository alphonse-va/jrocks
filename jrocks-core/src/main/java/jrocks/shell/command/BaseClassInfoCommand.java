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

  protected ClassInfoApi getClassInfoApi(ClassInfoParameterApi parameter) {

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

  protected void writeSource(String generatedSource, ClassInfoParameterApi parameter, ClassInfoApi classInfoApi) {

    String outputDurectory = classInfoApi.getSourceClassPath().getAbsolutePath();

    // TODO encapsulate project configs
    int outputDirectoryIdx = getProjectConfig().getOutputDirectories().indexOf(outputDurectory);
    String destDirectory = getProjectConfig().getSourceDirectories().get(outputDirectoryIdx);

    Path path = Paths.get(destDirectory + File.separator + classInfoApi.canonicalName().replace(".", File.separator) + parameter.suffix() + ".java");
    File file = path.toFile();
    if (file.exists() && !parameter.isForce()) {
      getLogger().error("%s file exists, please add --force if you want to overwrite", path.toString());
      return;
    }
    try {
      File dir = new File(destDirectory);
      if (!dir.isDirectory()) {
        boolean success = dir.mkdirs();
        if (success) {
          getLogger().info("Created path: " + dir.getPath());
        } else {
          getLogger().info("Could not create path: " + dir.getPath());
        }
      } else {
        getLogger().info("Path exists: " + dir.getPath());
      }
      Path savedFile = Files.write(path, generatedSource.getBytes());
      getLogger().info("%s class generated with success.", savedFile.getFileName().toFile().getAbsolutePath());
      getLogger().verbose("Generated source: \n\n%s", generatedSource);
    } catch (IOException e) {
      throw new IllegalStateException(format("Enable to create '%s' file!", destDirectory), e);
    }
  }
}
