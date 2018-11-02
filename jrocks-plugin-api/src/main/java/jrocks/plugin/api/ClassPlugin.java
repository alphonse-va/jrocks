package jrocks.plugin.api;

import java.util.List;

public interface ClassPlugin {

  String names();

  String description();

  List<String> keys();

  default String group() {
    return "Class";
  }

  List<GeneratedSource> generateSources(ClassParameter parameter, ClassApi clazz);
}
