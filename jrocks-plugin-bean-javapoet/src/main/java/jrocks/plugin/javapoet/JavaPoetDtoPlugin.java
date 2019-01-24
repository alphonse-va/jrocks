package jrocks.plugin.javapoet;

import jrocks.plugin.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.*;

@Component
public class JavaPoetDtoPlugin implements JRocksPlugin {

  static final String LAYOUT_QUALIFIER = "JavaPoetDtoPlugin";
  static final String WITH_MAPPER_FLAG = "with-mapper";
  static final String Q_DTO_PACKAGE = "dto.package";
  static final String Q_MAPPER_PACKAGE = "mapper.package";

  @Value("${jrocks.version}")
  private String version;

  @Value("${jrocks.plugin.dto.defaultSuffix:DTO}")
  private String defaultSuffix;

  private final List<PluginGenerator> generators;

  @Autowired
  public JavaPoetDtoPlugin(@Qualifier(LAYOUT_QUALIFIER) List<PluginGenerator> generators) {
    this.generators = generators;
  }

  @Override
  public String defaultSuffix() {
    return defaultSuffix;
  }

  @Override
  public String name() {
    return "dto-javapoet";
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public String description() {
    return "JavaPoet DTO Example";
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
  public List<String> additionalFlags() {
    return Collections.singletonList(WITH_MAPPER_FLAG);
  }

  @Override
  public Map<Object, Question> additionalQuestions(ClassParameterApi parameter, ClassApi classInfo) {
    HashMap<Object, Question> result = new LinkedHashMap<>();
    result.put(Q_DTO_PACKAGE, new QuestionSupport()
        .setBuffer(classInfo.packageName())
        .setQuestion("Enter the Dto _package_ please"));
    if (parameter.hasFlag(WITH_MAPPER_FLAG)) {
      result.put(Q_MAPPER_PACKAGE, new QuestionSupport()
          .setBuffer(classInfo.packageName() + ".mapper")
          .setQuestion("A mapper.. Really? Sure?? :-) Enter the package name please"));
    }
    return result;
  }
}
