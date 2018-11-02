package jrocks.shell.generator;

import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoParameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

public abstract class BaseTemplateGenerator implements TemplateGenerator {

  private TemplateWriterService writerService;

  protected void writeSource(String generatedSource, ClassInfoParameter parameter, ClassInfo classInfo) {
    writerService.writeClass(generatedSource, parameter, classInfo);
  }

  @Lazy
  @Autowired
  public void setWriterService(TemplateWriterService writerService) {
    this.writerService = writerService;
  }
}
