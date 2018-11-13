package jrocks.plugin.api;

import java.io.File;

public interface GeneratedSource {

  String filename();

  String content();

  File path();

  default String packageName() {
    return null;
  }

  default boolean isJava() {
    return packageName() != null;
  }
}
