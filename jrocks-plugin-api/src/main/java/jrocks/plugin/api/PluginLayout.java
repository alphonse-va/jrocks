package jrocks.plugin.api;

import java.util.List;

public interface PluginLayout {

  String name();

  default String description() {
    return "";
  }

  default String version() {
    return "beta";
  }

  List<GeneratedSource> generate(ClassParameter parameter, ClassApi classApi);
}
