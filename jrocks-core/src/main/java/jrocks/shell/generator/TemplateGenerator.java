package jrocks.shell.generator;

import jrocks.model.ClassInfoApi;
import jrocks.model.ClassInfoParameterApi;

import java.util.ArrayList;
import java.util.List;

public interface TemplateGenerator {

  void generateSource(ClassInfoParameterApi parameter, ClassInfoApi classInfo);

  String getName();

  default List<String> getAdditionalFlags() {
    return new ArrayList<>();
  }

  String getSuffix();
}
