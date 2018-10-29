package jrocks.generator.bean.builder;

import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoParameter;
import jrocks.shell.generator.BaseTemplateGenerator;
import org.springframework.stereotype.Component;

@Component
public class BuilderTemplateGenerator extends BaseTemplateGenerator {

  @Override
  public void generateSource(ClassInfoParameter parameter, ClassInfo classInfo) {
    String generatedSource = builder.template(classInfo, parameter).render().toString();
    writeSource(generatedSource, parameter, classInfo);
  }

  @Override
  public String suffix() {
    return "Builder";
  }
}
