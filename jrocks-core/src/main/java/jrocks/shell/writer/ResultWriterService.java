package jrocks.shell.writer;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.ClassParameterApi;
import jrocks.plugin.api.GeneratedSource;

public interface ResultWriterService {

  void writeClass(GeneratedSource generatedSource, ClassParameterApi parameter, ClassApi clazz);
}
