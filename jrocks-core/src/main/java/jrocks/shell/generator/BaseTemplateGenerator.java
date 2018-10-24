package jrocks.shell.generator;

import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoParameter;
import jrocks.shell.TerminalLogger;
import jrocks.shell.config.ConfigService;
import jrocks.shell.config.ModuleConfig;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static java.lang.String.format;

public abstract class BaseTemplateGenerator implements TemplateGenerator {

  private TerminalLogger logger;
  private ConfigService configService;

  @Autowired
  protected BaseTemplateGenerator(ConfigService configService, TerminalLogger logger) {
    this.configService = configService;
    this.logger = logger;
  }

  protected TerminalLogger getLogger() {
    return logger;
  }

  protected void writeSource(String generatedSource, ClassInfoParameter parameter, ClassInfo classInfo) {

    String outputDurectory = classInfo.getSourceClassPath().getAbsolutePath();

    Set<ModuleConfig> modules = configService.getConfig().getModules();
    String destDirectory = modules.stream()
        .filter(config -> config.getOutputDirectory().equals(outputDurectory))
        .map(ModuleConfig::getSourceDirectory)
        .findAny()
        .orElse(modules.iterator().next().getOutputDirectory());

    // TODO cleanups file writing
    Path path = Paths.get(destDirectory + File.separator + classInfo.canonicalName().replace(".", File.separator) + parameter.suffix() + ".java");
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
