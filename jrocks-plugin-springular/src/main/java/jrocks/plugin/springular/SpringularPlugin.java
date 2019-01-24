package jrocks.plugin.springular;

import jrocks.plugin.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SpringularPlugin implements JRocksPlugin {

  static final String LAYOUT_QUALIFIER = "SpringularPlugin";

  static final String Q_PACKAGE = "PACKAGE";

  @Value("${jrocks.version}")
  private String version;

  @Value("${classpath*:/springular-plugin.yml}")
  private Resource pluginConfigFile;

  private final List<PluginGenerator> generators;

  @Autowired
  public SpringularPlugin(@Qualifier(LAYOUT_QUALIFIER) List<PluginGenerator> generators) {
    this.generators = generators;
  }

  @Override
  public String name() {
    return "springular";
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public String description() {
    return "Springular Generator";
  }

  @Override
  public String defaultSuffix() {
    return "FIXME: we don't always need to specify a suffix";
  }

  @Override
  public List<PluginGenerator> generators() {
    return generators;
  }

  @Override
  public Path configFile() {
    try {
      Path path = Files.createTempFile("springular-plugin-", ".yml");
      Files.copy(pluginConfigFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
      return path;
    } catch (IOException e) {
      throw new JRocksApiException(e);
    }
  }

  @Override
  public Map<Object, Question> additionalQuestions(ClassParameterApi parameter, ClassApi classInfo) {
    HashMap<Object, Question> result = new HashMap<>();
    result.put(Q_PACKAGE, new QuestionSupport()
        .setBuffer(classInfo.packageName())
        .setQuestion("Package name"));
    return result;
  }
}
