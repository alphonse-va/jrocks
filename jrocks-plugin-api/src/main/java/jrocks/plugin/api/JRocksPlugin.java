package jrocks.plugin.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface JRocksPlugin extends JRocksBean {

  String version();

  String description();

  List<String> keys();

  default String group() {
    return "Class";
  }

  default List<String> additionalFlags() {
    return new ArrayList<>();
  }

  default Map<Object, Question> additionalQuestions(ClassApi classInfo) {
    return new HashMap<>();
  }

  String defaultSuffix();

  List<PluginGenerator> generators();
}

