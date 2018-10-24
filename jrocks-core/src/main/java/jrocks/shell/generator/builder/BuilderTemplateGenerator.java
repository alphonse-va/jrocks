package jrocks.shell.generator.builder;

import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoParameter;
import jrocks.shell.TerminalLogger;
import jrocks.shell.config.ConfigService;
import jrocks.shell.generator.BaseTemplateGenerator;
import jrocks.template.bean.builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuilderTemplateGenerator extends BaseTemplateGenerator {

  @Autowired
  protected BuilderTemplateGenerator(ConfigService configService, TerminalLogger logger) {
    super(configService, logger);
  }

  @Override
  public void generateSource(ClassInfoParameter parameter, ClassInfo classInfo) {
    String generatedSource = builder.template(classInfo, parameter).render().toString();
    writeSource(generatedSource, parameter, classInfo);
  }

  @Override
  public String getName() {
    return "BUILDER";
  }

  @Override
  public String getSuffix() {
    return "Builder";
  }
}
