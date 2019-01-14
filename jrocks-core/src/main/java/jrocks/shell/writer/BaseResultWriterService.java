package jrocks.shell.writer;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.ClassParameterApi;
import jrocks.plugin.api.GeneratedSource;
import jrocks.plugin.api.config.ConfigService;
import jrocks.plugin.api.config.ModuleConfig;
import jrocks.shell.JRocksShellException;
import jrocks.plugin.api.shell.TerminalLogger;
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

  private ConfigService configService;

  private TerminalLogger terminalLogger;

  @Autowired
  public BaseResultWriterService(ConfigService configService, TerminalLogger terminalLogger) {
    this.configService = configService;
    this.terminalLogger = terminalLogger;
  }

  @Override
  public void writeFile(GeneratedSource generatedSource, ClassParameterApi parameter, ClassApi clazz) {
    String destDirectory;
    if (generatedSource.isJava()) {
      String outDirectory = getJavaOutputDirectory(clazz);
      destDirectory = getJavaDestDirectory(generatedSource, outDirectory);
    } else {
      destDirectory = generatedSource.path().getAbsolutePath();
    }

    Path path = Paths.get(destDirectory + File.separator + generatedSource.filename());
    File file = path.toFile();
    if (file.exists() && !parameter.isForce()) {
      terminalLogger.error(this, "_%s_ file exists, please user _--force_ if you want to overwrite", path.toString());
      return;
    }
    try {
      File dir = new File(destDirectory);
      if (!dir.isDirectory()) {
        boolean success = dir.mkdirs();
        if (success) {
          terminalLogger.info(this, "created path: _%s_", dir.getPath());
        } else {
          terminalLogger.error(this, "could not create path: _%s_", dir.getPath());
        }
      }
      terminalLogger.info(this, "File _%s_", path);
      String content = generatedSource.content();
      if (terminalLogger.isVerbose() || parameter.isDryRun()) {
        terminalLogger.newline();
        terminalLogger.setMessagePrefix("  |  ");
        Stream.of(content.split("\n"))
            .forEach(line -> {
              if (parameter.isDryRun()) terminalLogger.info("  %s", line);
              else terminalLogger.verbose("  %s", line);
            });
        terminalLogger.setDefaultMessagePrefix();
        terminalLogger.newline();
      }
      if (!parameter.isDryRun()) {
        Path savedFile = Files.write(path, content.getBytes());
        terminalLogger.info(this, "_%s_ created with success.", savedFile.toFile().getAbsolutePath());
      }
    } catch (IOException e) {
      throw new JRocksShellException(format("Enable to create *%s* file!", destDirectory), e);
    }
  }

  private String getJavaDestDirectory(GeneratedSource generatedSource, String outDirectory) {
    return outDirectory + File.separator + generatedSource.packageName().replace(".", File.separator);
  }

  private String getJavaOutputDirectory(ClassApi clazz) {
    String outputDirectory = clazz.sourceClassPath().getAbsolutePath();
    Set<ModuleConfig> modules = configService.getConfig().getModules();
    return modules.stream()
        .filter(config -> config.getOutputDirectory().equals(outputDirectory))
        .map(ModuleConfig::getSourceDirectory)
        .findAny()
        .orElse(modules.iterator().next().getSourceDirectory());
  }

  @Override
  public String name() {
    return "writer";
  }
}
