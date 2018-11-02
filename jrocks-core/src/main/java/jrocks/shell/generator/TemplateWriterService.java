package jrocks.shell.generator;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.ClassParameter;

public interface TemplateWriterService {

  void writeClass(String generatedSource, ClassParameter parameter, ClassApi clazz);
}
