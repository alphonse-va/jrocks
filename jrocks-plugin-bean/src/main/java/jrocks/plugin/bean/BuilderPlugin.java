package jrocks.plugin.bean;

import jrocks.plugin.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.singletonList;

@Component
public class BuilderPlugin implements JRocksPlugin {

  static final String LAYOUT_QUALIFIER = "BuilderLayout";

  static final String Q_PACKAGE = "PACKAGE";

  @Value("${jrocks.version}")
  private String version;

  private final List<PluginLayout> layouts;

  @Autowired
  public BuilderPlugin(@Qualifier(LAYOUT_QUALIFIER) List<PluginLayout> layouts) {
    this.layouts = layouts;
  }

  @Override
  public String name() {
    return "builder";
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public String description() {
    return "Generate a builder for selected class";
  }

  @Override
  public List<String> keys() {
    return singletonList("builder");
  }

  @Override
  public String defaultSuffix() {
    return "Builder";
  }

  @Override
  public List<PluginLayout> layouts() {
    return layouts;
  }

  @Override
  public String layoutQualifier() {
    return LAYOUT_QUALIFIER;
  }

  @Override
  public Map<Object, Question> additionalQuestions(ClassApi classInfo) {
    HashMap<Object, Question> result = new HashMap<>();
    result.put(Q_PACKAGE, new QuestionSupport()
        .setBuffer(classInfo.packageName())
        .setQuestion("Do you want ot use _" + classInfo.packageName() + "_ as destination package?"));
    return result;
  }
}
