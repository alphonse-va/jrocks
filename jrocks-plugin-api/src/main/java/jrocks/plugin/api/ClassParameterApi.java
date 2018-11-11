package jrocks.plugin.api;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ClassParameterApi {

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

  void addResponses(Map<Object, QuestionResponse> responses);

  Map<Object, QuestionResponse> responses();

  PluginLayout getLayout();
}
