package jrocks.template.util;

import jrocks.plugin.api.config.ConfigService;
import jrocks.plugin.api.config.ModuleConfig;
import jrocks.plugin.api.config.ModuleType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
class AngularUtil {

  private final ConfigService configService;

  @Autowired
  public AngularUtil(ConfigService configService) {
    this.configService = configService;
  }


  String baseDir() {
    return getAngularModule().getBaseDirectory();
  }

  String appDir() {
    return baseDir() + "/src/app";
  }

  void importType(String source, String target) {
    try {
      Path sourcePath = Paths.get(source);
      Path targetPath = Paths.get(target);
      String tsType = extractType(sourcePath, targetPath);

      if (isImported(tsType, targetPath)) {
        System.out.println("Class " + tsType + " already imported into " + target + ", skipped!");
        return;
      }

      String importLine = generateImport(tsType, sourcePath, targetPath);
      String modifiedSource = insertImport(targetPath, importLine);
      Files.write(targetPath, modifiedSource.getBytes());
    } catch (IOException e) {
      throw new JRocksTemplateUtilException(e);
    }
  }

  private boolean isImported(String tsType, Path target) {
    try {
      return Files.readAllLines(target).stream().anyMatch(l -> l.replaceAll(" ", "").contains("{" + tsType + "}"));
    } catch (IOException e) {
      throw new JRocksTemplateUtilException("Error while reading target file: " + target, e);
    }
  }

  private String generateImport(String componentName, Path source, Path target) {
    Path relativePath = target.getParent().relativize(source.getParent());
    String sourceName = source.toFile().getName().replace(".ts", "");
    Path sourceRelativePath = Paths.get(relativePath.toString(), sourceName);

    String importString = "import {%s} from \"%s\";";
    if (!sourceRelativePath.toString().startsWith(".")) {
      importString = "import {%s} from \"./%s\";";
    }
    return String.format(importString, componentName, sourceRelativePath);
  }

  private String insertImport(Path target, String angularImport) {
    try {
      String content = String.join("\n", Files.readAllLines(target));
      int index = 0;
      Matcher m = Pattern.compile("import[ ]+\\{.*").matcher(content);
      while (m.find()) {
        index = m.end();
      }

      String begin = content.substring(0, index);
      String end = content.substring(index);

      return begin + "\n" + angularImport + end;
    } catch (IOException e) {
      throw new JRocksTemplateUtilException(e);
    }
  }

  private String extractType(Path source, Path target) {
    try {
      return Files.readAllLines(source)
          .stream()
          .filter(l -> l.trim().startsWith("export"))
          .map(this::extractTypeInLine)
          .collect(Collectors.joining());
    } catch (IOException e) {
      throw new JRocksTemplateUtilException(e);
    }
  }

  private String extractTypeInLine(String line) {
    Matcher matcher = Pattern.compile("^export[ ]+class[ ]+([a-z|A-Z]+)").matcher(line);
    if (matcher.find()) {
      return matcher.group(1);
    }
    return "";
  }

  private ModuleConfig getAngularModule() {
    return configService.getConfig().findModuleByType(ModuleType.ANGULAR)
        .orElseThrow(() -> new IllegalStateException("A module of type ANGULAR is required by this generator, please check your config."));
  }

}
