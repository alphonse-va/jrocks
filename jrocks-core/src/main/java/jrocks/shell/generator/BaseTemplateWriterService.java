package jrocks.shell.generator;

import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoParameter;
import jrocks.shell.TerminalLogger;
import jrocks.shell.config.ConfigService;
import jrocks.shell.config.ModuleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static java.lang.String.format;

@Service
public class BaseTemplateWriterService implements TemplateWriterService {

  private ConfigService configService;

  private TerminalLogger terminalLogger;

  @Autowired
  public BaseTemplateWriterService(ConfigService configService, TerminalLogger terminalLogger) {
    this.configService = configService;
    this.terminalLogger = terminalLogger;
  }

  @Override
  public void writeTemplate(String generatedSource, ClassInfoParameter parameter, ClassInfo classInfo) {
    String outputDirectory = classInfo.getSourceClassPath().getAbsolutePath();

    Set<ModuleConfig> modules = configService.getConfig().getModules();
    String destDirectory = modules.stream()
        .filter(config -> config.getOutputDirectory().equals(outputDirectory))
        .map(ModuleConfig::getSourceDirectory)
        .findAny()
        .orElse(modules.iterator().next().getOutputDirectory());

    // TODO cleanups file writing
    Path path = Paths.get(destDirectory + File.separator + classInfo.canonicalName().replace(".", File.separator) + parameter.suffix() + ".java");
    File file = path.toFile();
    if (file.exists() && !parameter.isForce()) {
      terminalLogger.error("%s file exists, please user --force if you want to overwrite", path.toString());
      return;
    }
    try {
      File dir = new File(destDirectory);
      if (!dir.isDirectory()) {
        boolean success = dir.mkdirs();
        if (success) {
          terminalLogger.info("Created path: " + dir.getPath());
        } else {
          terminalLogger.info("Could not create path: " + dir.getPath());
        }
      }
      Path savedFile = Files.write(path, generatedSource.getBytes());
      terminalLogger.info("%s class generated with success.", savedFile.toFile().getAbsolutePath());
      terminalLogger.verbose("Generated source: \n\n%s\n", generatedSource);
    } catch (IOException e) {
      throw new IllegalStateException(format("Enable to create '%s' file!", destDirectory), e);
    }
  }
}
