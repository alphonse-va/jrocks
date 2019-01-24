package jrocks.template.util;

import jrocks.plugin.api.shell.TerminalLogger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
class FileUtil {

  private final TerminalLogger terminalLogger;

  @Autowired
  public FileUtil(TerminalLogger terminalLogger) {
    this.terminalLogger = terminalLogger;
  }

  public Optional<File> addContentAfterMarker(String filePath, String content, String marker) {
    Path path = Paths.get(filePath);

    try {
      String currentTag = "";
      for (String line : Files.lines(path).collect(Collectors.toList())) {
        if (line.contains(marker)) {
          currentTag = marker;
        }

        if (currentTag.equals(marker) && line.contains(content.trim())) {
          terminalLogger.warning("Content _%s_ for tag _%s_ already exist into _%s_, skipped!", content, marker, path.toFile().getName());
          return Optional.empty();
        }
      }
    } catch (IOException e) {
      throw new JRocksTemplateUtilException(e);
    }
    try (Stream<String> lines = Files.lines(path)) {
      List<String> replaced = lines
          .map(line -> line.replace(marker, marker + "\n" + content))
          .collect(Collectors.toList());
      return Optional.of(Files.write(path, replaced).toFile());
    } catch (IOException e) {
      throw new JRocksTemplateUtilException(e);
    }
  }
}
