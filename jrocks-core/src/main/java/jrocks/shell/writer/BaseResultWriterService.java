package jrocks.shell.writer;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.ClassParameterApi;
import jrocks.shell.JRocksShellException;
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
import java.util.stream.Stream;

import static java.lang.String.format;

@Service
public class BaseResultWriterService implements ResultWriterService {

  private static final String TERM_NAME = "[writer]";

  private ConfigService configService;

  private TerminalLogger terminalLogger;

  @Autowired
  public BaseResultWriterService(ConfigService configService, TerminalLogger terminalLogger) {
    this.configService = configService;
    this.terminalLogger = terminalLogger;
  }

  @Override
  public void writeClass(String generatedSource, ClassParameterApi parameter, ClassApi clazz) {
    String outputDirectory = clazz.getSourceClassPath().getAbsolutePath();

    Set<ModuleConfig> modules = configService.getConfig().getModules();
    String destDirectory = modules.stream()
        .filter(config -> config.getOutputDirectory().equals(outputDirectory))
        .map(ModuleConfig::getSourceDirectory)
        .findAny()
        .orElse(modules.iterator().next().getSourceDirectory());

    // TODO cleanups file writing
    Path path = Paths.get(destDirectory + File.separator + clazz.name().replace(".", File.separator) + parameter.suffix() + ".java");
    File file = path.toFile();
    if (file.exists() && !parameter.isForce()) {
      terminalLogger.error("*%s* _%s_ file exists, please user _--force_ if you want to overwrite", TERM_NAME, path.toString());
      return;
    }
    try {
      File dir = new File(destDirectory);
      if (!dir.isDirectory()) {
        boolean success = dir.mkdirs();
        if (success) {
          terminalLogger.info("*%s* created path: _%s_", TERM_NAME, dir.getPath());
        } else {
          terminalLogger.info("*%s* could not create path: _%s_", TERM_NAME, dir.getPath());
        }
      }
      if (terminalLogger.isVerbose() || parameter.isDryRun()) {
        terminalLogger.newline();
        terminalLogger.setMessagePrefix("  |  ");
        Stream.of(generatedSource.split("\n"))
            .forEach(l -> {
              if (parameter.isDryRun())
                terminalLogger.info("  %s", l);
              else
                terminalLogger.verbose("  %s", l);
            });
        terminalLogger.setDefaultMessagePrefix();
        terminalLogger.newline();
      }
      if (!parameter.isDryRun()) {
        Path savedFile = Files.write(path, generatedSource.getBytes());
        terminalLogger.info("*%s* _%s_ class created with success.", TERM_NAME, savedFile.toFile().getAbsolutePath());
      }
    } catch (IOException e) {
      throw new JRocksShellException(format("Enable to create *%s* file!", destDirectory), e);
    }
  }
}
