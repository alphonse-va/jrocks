package jrocks.plugin.api;

import java.util.*;

import static java.util.Collections.singletonList;

public interface JRocksPlugin extends JRocksBean {

  String version();

  String description();

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

  default List<String> keys() {
    return singletonList(name());
  }
}

