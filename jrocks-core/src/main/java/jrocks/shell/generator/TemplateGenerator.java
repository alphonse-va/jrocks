package jrocks.shell.generator;

import jrocks.api.ClassInfoApi;
import jrocks.api.ClassInfoParameterApi;

import java.util.ArrayList;
import java.util.List;

public interface TemplateGenerator {

  void generateSource(ClassInfoParameterApi parameter, ClassInfoApi classInfo);

  String getName();

  default List<String> getAddtionalFlags() {
    return new ArrayList<>();
  }

  String getSuffix();
}
