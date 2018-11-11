package jrocks.model;

import jrocks.plugin.api.ClassParameterApi;
import jrocks.plugin.api.PluginLayout;
import jrocks.plugin.api.QuestionResponse;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface ClassInfoParameterApi extends ClassParameterApi {

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

  Map<Object, QuestionResponse> responses();

  List<String> getAdditionalFlags();

  PluginLayout getLayout();

}
