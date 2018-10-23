package jrocks.shell.generator;

import jrocks.api.ClassInfoApi;
import jrocks.api.ClassInfoParameterApi;
import jrocks.shell.TerminalLogger;
import jrocks.shell.config.JRocksProjectConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;

public abstract class BaseTemplateGenerator implements TemplateGenerator {

  private TerminalLogger logger;
  private JRocksProjectConfig projectConfig;

  @Autowired
  protected BaseTemplateGenerator(JRocksProjectConfig projectConfig, TerminalLogger logger) {
    this.projectConfig = projectConfig;
    this.logger = logger;
  }

  protected TerminalLogger getLogger() {
    return logger;
  }

  public JRocksProjectConfig getProjectConfig() {
    return projectConfig;
  }

  protected void writeSource(String generatedSource, ClassInfoParameterApi parameter, ClassInfoApi classInfoApi) {

    String outputDurectory = classInfoApi.getSourceClassPath().getAbsolutePath();

    // TODO encapsulate project configs
    int outputDirectoryIdx = getProjectConfig().getOutputDirectories().indexOf(outputDurectory);
    String destDirectory = getProjectConfig().getSourceDirectories().get(outputDirectoryIdx);

    // TODO cleanups file writhing
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
      }
      Path savedFile = Files.write(path, generatedSource.getBytes());
      getLogger().info("%s class generated with success.", savedFile.toFile().getAbsolutePath());
      getLogger().verbose("Generated source: \n\n%s\n", generatedSource);
    } catch (IOException e) {
      throw new IllegalStateException(format("Enable to create '%s' file!", destDirectory), e);
    }
  }
}
