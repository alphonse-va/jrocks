package jrocks.plugin.api;

import java.util.ArrayList;
import java.util.List;

public interface JRocksPlugin {

  String name();

  String description();

  List<String> keys();

  default String group() {
    return "Class";
  }

  List<GeneratedSource> generateSources(ClassParameter parameter, ClassApi clazz);


  default List<String> additionalFlags() {
    return new ArrayList<>();
  }
}
