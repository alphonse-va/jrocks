package jrocks.plugin.bean;

import jrocks.plugin.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class BuilderPlugin implements JRocksPlugin {

  static final String LAYOUT_QUALIFIER = "BuilderLayout2";

  static final String Q_PACKAGE = "PACKAGE";

  @Value("${jrocks.version}")
  private String version;

  @Value("${jrocks.plugin.builder.defaultSuffix:Builder}")
  private String defaultSuffix;

  private final List<PluginGenerator> generators;

  @Autowired
  public BuilderPlugin(@Qualifier(LAYOUT_QUALIFIER) List<PluginGenerator> generators) {
    this.generators = generators;
  }

  @Override
  public String name() {
    return "builder2";
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public String description() {
    return "Builder Generator";
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
  public Map<Object, Question> additionalQuestions(ClassParameterApi parameter, ClassApi classInfo) {
    HashMap<Object, Question> result = new HashMap<>();
    result.put(Q_PACKAGE, new QuestionSupport()
        .setBuffer(classInfo.packageName())
        .setQuestion("Package name"));
    return result;
  }
}
