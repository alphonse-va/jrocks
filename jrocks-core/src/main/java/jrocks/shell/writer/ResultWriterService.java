package jrocks.shell.writer;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.ClassParameterApi;
import jrocks.plugin.api.GeneratedSource;
import jrocks.plugin.api.JRocksBean;

public interface ResultWriterService extends JRocksBean {

  void writeFile(GeneratedSource generatedSource, ClassParameterApi parameter, ClassApi clazz);
}
