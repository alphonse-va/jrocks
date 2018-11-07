package jrocks.shell.writer;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.ClassParameterApi;

public interface ResultWriterService {

  void writeClass(String generatedSource, ClassParameterApi parameter, ClassApi clazz);
}
