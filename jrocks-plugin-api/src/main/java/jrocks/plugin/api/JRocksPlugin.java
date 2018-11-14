package jrocks.plugin.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface JRocksPlugin extends JRocksBean {

  String version();

  String description();

  List<String> keys();

  String defaultSuffix();

  List<PluginGenerator> generators();

  default String group() {
    return "> JRocks Plugins";
  }

  default List<String> additionalFlags() {
    return new ArrayList<>();
  }

  default Map<Object, Question> additionalQuestions(ClassParameterApi parameter, ClassApi classInfo) {
    return new HashMap<>();
  }
}

