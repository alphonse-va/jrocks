package jrocks.shell.generator;

import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoParameter;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseTemplateGenerator implements TemplateGenerator {

  private TemplateWriterService writerService;

  @Autowired
  protected BaseTemplateGenerator(TemplateWriterService writerService) {
    this.writerService = writerService;
  }

  protected void writeSource(String generatedSource, ClassInfoParameter parameter, ClassInfo classInfo) {
    writerService.writeTemplate(generatedSource, parameter, classInfo);
  }
}
