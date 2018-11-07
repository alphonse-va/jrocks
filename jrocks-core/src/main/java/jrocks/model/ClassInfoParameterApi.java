package jrocks.model;

import jrocks.plugin.api.ClassParameterApi;
import jrocks.plugin.api.PluginLayout;

import java.io.File;
import java.util.List;

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

  List<String> getAdditionalFlags();

  PluginLayout getLayout();

}
