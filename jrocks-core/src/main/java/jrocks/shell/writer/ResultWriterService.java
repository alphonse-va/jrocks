package jrocks.shell.writer;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.ClassParameter;

public interface ResultWriterService {

  void writeClass(String generatedSource, ClassParameter parameter, ClassApi clazz);
}
