package jrocks.plugin.javapoet;

import jrocks.plugin.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JavaPoetBuilderPlugin implements JRocksPlugin {

  static final String LAYOUT_QUALIFIER = "JavaPoetBuilderPlugin";

  static final String Q_PACKAGE = "PACKAGE";

  @Value("${jrocks.version}")
  private String version;

  @Value("${jrocks.plugin.dto.defaultSuffix:Builder}")
  private String defaultSuffix;

  private final List<PluginGenerator> generators;

  @Autowired
  public JavaPoetBuilderPlugin(@Qualifier(LAYOUT_QUALIFIER) List<PluginGenerator> generators) {
    this.generators = generators;
  }

  @Override
  public String name() {
    return "builder-javapoet";
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public String description() {
    return "JavaPoet Builder Example";
  }

  @Override
  public String defaultSuffix() {
    return defaultSuffix;
  }

  @Override
  public List<PluginGenerator> generators() {
    return generators;
  }

  @Override
  public Path configFile() {
    return null;
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
