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

  default List<String> additionalFlags() {
    return new ArrayList<>();
  }

  String defaultSuffix();

  List<PluginLayout> layouts();

  String layoutQualifier();
}

