package jrocks.model;

import jrocks.plugin.api.ClassParameter;
import jrocks.plugin.api.PluginLayout;

import java.io.File;
import java.util.List;

public interface ClassInfoParameter extends ClassParameter {

  List<String> excludedFields();

  List<String> includedFields();

  List<String> mandatoryFields();

  String classCanonicalName();

  boolean isForce();

  String suffix();

  String suffixToRemove();

  boolean toInclude(String field);

  boolean toExclude(String field);

  File getFile();

  List<String> getAdditionalFlags();

  PluginLayout getLayout();
}
