package jrocks.model;

import java.io.File;
import java.util.ArrayList;
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

  File getFile();

  List<String> getAdditionalFlags();
}
