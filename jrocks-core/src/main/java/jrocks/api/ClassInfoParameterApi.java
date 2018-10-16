package jrocks.api;

import java.util.List;

public interface ClassInfoParameterApi {

  List<String> getExcludedFields();

  List<String> getIncludedFields();

  List<String> getMandatoryFields();

  String getClassCanonicalName();

  boolean isForce();

  String suffix();

  String suffixToRemove();

  boolean toInclude(String field);

  boolean toExclude(String field);
}
