package jrocks.plugin.api;

import java.util.List;

public interface PluginLayout {

  String name();

  default String description() {
    return "Not provided";
  }

  default String version() {
    return "Not provided";
  }

  List<GeneratedSource> generate(ClassParameter parameter, ClassApi classApi);
}
