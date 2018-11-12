package jrocks.plugin.api;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

  Optional<QuestionResponse> getUserResponse(Object key);

  List<String> additionalFlags();

  boolean hasFlag(String flag);

  void addResponses(Map<Object, QuestionResponse> responses);

  Map<Object, QuestionResponse> responses();

  PluginGenerator getLayout();
}
