package jrocks.plugin.api;

import java.util.List;

public interface PluginGenerator extends JRocksBean {

  String description();

  String version();

  List<GeneratedSource> generate(ClassParameterApi parameter, ClassApi classApi);
}
