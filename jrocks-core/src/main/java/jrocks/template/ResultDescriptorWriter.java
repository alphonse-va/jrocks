package jrocks.template;

import jrocks.plugin.api.JRocksBean;
import jrocks.plugin.api.shell.TerminalLogger;
import jrocks.plugin.api.template.ResultDescriptor;
import jrocks.shell.JRocksShellException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

@Component
class ResultDescriptorWriter implements JRocksBean {

  private final TerminalLogger terminalLogger;

  @Autowired
  public ResultDescriptorWriter(TerminalLogger logger) {
    terminalLogger = logger;
  }

  void write(ResultDescriptor resultDescriptor) {

    File file = resultDescriptor.getFile();
    File dir = file.getParentFile();

    boolean force = resultDescriptor.getContext().parameter().isForce();
    boolean dryRun = resultDescriptor.getContext().parameter().isDryRun();

      if (file.exists() && (!force && !dryRun)) {
      terminalLogger.error(this, "_%s_ file exists, please user _--force_ if you want to overwrite", file.getName());
      return;
    }
    try {
      if (!dryRun && (!dir.exists() || !dir.isDirectory())) {
        boolean success = dir.mkdirs();
        if (success) {
          terminalLogger.info(this, "created path: _%s_", dir.getPath());
        } else {
          terminalLogger.error(this, "could not create path: _%s_", dir.getPath());
        }
      }
      String content = resultDescriptor.getContent();
      if (terminalLogger.isVerbose()) {
        terminalLogger.newline();
        terminalLogger.setMessagePrefix("  |  ");
        Stream.of(content.split("\n")).forEach(line -> terminalLogger.verbose("  %s", line));
        terminalLogger.setDefaultMessagePrefix();
        terminalLogger.newline();
      }
      if (!dryRun) {
        Path savedFile = Files.write(file.toPath(), content.getBytes());
        terminalLogger.info(this, "_%s_ created with success.", savedFile.toFile().getAbsolutePath());
      }
    } catch (IOException e) {
      throw new JRocksShellException(String.format("Enable to create *%s* file!", dir), e);
    }
  }

  @Override
  public String name() {
    return "result-writer";
  }
}
