package jrocks.shell.generator;

import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoParameter;

import java.util.ArrayList;
import java.util.List;

public interface TemplateGenerator {

  void generateSource(ClassInfoParameter parameter, ClassInfo classInfo);

  default List<String> additionalFlags() {
    return new ArrayList<>();
  }

  String suffix();
}
