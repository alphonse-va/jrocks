package jrocks.shell.generator;

import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoParameter;

public interface TemplateWriterService {

  void writeTemplate(String generatedSource, ClassInfoParameter parameter, ClassInfo classInfo);
}
