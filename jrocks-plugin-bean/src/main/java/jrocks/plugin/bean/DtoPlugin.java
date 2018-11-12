package jrocks.plugin.bean;

import jrocks.plugin.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DtoPlugin implements JRocksPlugin {

  static final String LAYOUT_QUALIFIER = "DtoLayout";
  static final String WITH_MAPPER_FLAG = "with-mapper";

  static final String Q_DTO_PACKAGE = "dto.package";
  static final String Q_MAPPER_PACKAGE = "mapper.package";


  @Value("${jrocks.version}")
  private String version;

  private final List<PluginGenerator> layouts;

  @Autowired
  public DtoPlugin(@Qualifier(LAYOUT_QUALIFIER) List<PluginGenerator> layouts) {
    this.layouts = layouts;
  }

  @Override
  public String defaultSuffix() {
    return "DTO";
  }

  @Override
  public String name() {
    return "dto";
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public String description() {
    return "DTO Generator";
  }

  @Override
  public List<String> keys() {
    return Collections.singletonList("dto");
  }

  @Override
  public List<PluginGenerator> generators() {
    return layouts;
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
        .setQuestion("Dto package"));
    if (parameter.hasFlag(WITH_MAPPER_FLAG)) {
      result.put(Q_MAPPER_PACKAGE, new QuestionSupport()
          .setBuffer(classInfo.packageName() + ".mapper")
          .setQuestion("Mapper package"));
    }
    return result;
  }
}
