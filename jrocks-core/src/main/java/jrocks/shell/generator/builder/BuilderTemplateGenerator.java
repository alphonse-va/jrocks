package jrocks.shell.generator.builder;

import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoParameter;
import jrocks.shell.generator.BaseTemplateGenerator;
import jrocks.shell.generator.TemplateWriterService;
import jrocks.template.bean.builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuilderTemplateGenerator extends BaseTemplateGenerator {

  @Autowired
  public BuilderTemplateGenerator(TemplateWriterService writerService) {
    super(writerService);
  }

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
