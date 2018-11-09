package jrocks.plugin.api;

import java.util.List;

public interface PluginLayout {

  String name();

  String description();

  String version();

  List<GeneratedSource> generate(ClassParameterApi parameter, ClassApi classApi);
}
