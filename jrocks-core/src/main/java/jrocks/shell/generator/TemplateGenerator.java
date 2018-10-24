package jrocks.shell.generator;

import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoParameter;

import java.util.ArrayList;
import java.util.List;

public interface TemplateGenerator {

  void generateSource(ClassInfoParameter parameter, ClassInfo classInfo);

  String getName();

  default List<String> getAdditionalFlags() {
    return new ArrayList<>();
  }

  String getSuffix();
}
