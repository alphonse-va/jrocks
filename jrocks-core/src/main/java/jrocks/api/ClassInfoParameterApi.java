package jrocks.api;

import jrocks.shell.LogLevel;

import java.util.List;

public interface ClassInfoParameterApi {

  List<String> getExcludedFields();

  List<String> getIncludedFields();

  List<String> getMandatoryFields();

  String getClassCanonicalName();

  LogLevel getLogLevel();

  boolean isForce();

  boolean toInclude(String field);

  boolean toExclude(String field);
}
