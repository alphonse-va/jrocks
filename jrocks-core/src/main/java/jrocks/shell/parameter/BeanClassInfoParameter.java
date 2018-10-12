package jrocks.shell.parameter;

import java.util.List;

public interface BeanClassInfoParameter {

  Class<?> getSourceClass();

  List<String> getExcludedFields();

  List<String> getIncludedFields();

  List<String> getMandatoryFields();
}
