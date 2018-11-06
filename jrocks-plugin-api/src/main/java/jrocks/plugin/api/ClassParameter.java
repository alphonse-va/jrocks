package jrocks.plugin.api;

import java.io.File;
import java.util.List;

public interface ClassParameter {

  List<String> excludedFields();

  List<String> includedFields();

  List<String> mandatoryFields();

  String classCanonicalName();

  boolean isForce();

  boolean isDryRun();

  String suffix();

  String suffixToRemove();

  boolean toInclude(String field);

  boolean toExclude(String field);

  File getFile();

  List<String> getAdditionalFlags();
}
