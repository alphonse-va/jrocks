package jrocks.shell.parameter;

import jrocks.api.ClassInfoParameterApi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BaseClassInfoParameter implements ClassInfoParameterApi {

  private String classCanonicalName;

  private List<String> excludedFields;

  private List<String> includedFields;

  private List<String> mandatoryFields;

  private String suffix;

  private String suffixToRemove;

  private boolean force;

  private File file;

  BaseClassInfoParameter(String classCanonicalName,
                         boolean force,
                         List<String> excludedFields,
                         List<String> includedFields,
                         List<String> mandatoryFields,
                         String suffix,
                         String suffixToRemove, File file) {
    this.classCanonicalName = classCanonicalName;
    this.force = force;
    this.excludedFields = excludedFields != null ? excludedFields : new ArrayList<>();
    this.includedFields = includedFields != null ? includedFields : new ArrayList<>();
    this.mandatoryFields = mandatoryFields != null ? mandatoryFields : new ArrayList<>();
    this.suffix = suffix;
    this.suffixToRemove = suffixToRemove;
    this.file = file;
  }

  protected BaseClassInfoParameter(ClassInfoParameterApi parameter) {
    this(parameter.getClassCanonicalName(),
        parameter.isForce(),
        parameter.getExcludedFields(),
        parameter.getIncludedFields(),
        parameter.getMandatoryFields(),
        parameter.suffix(),
        parameter.suffixToRemove(),
        parameter.getFile());
  }

  @Override
  public List<String> getExcludedFields() {
    return excludedFields;
  }

  @Override
  public List<String> getIncludedFields() {
    return includedFields;
  }

  @Override
  public List<String> getMandatoryFields() {
    return mandatoryFields;
  }

  @Override
  public String getClassCanonicalName() {
    return classCanonicalName;
  }

  @Override
  public boolean isForce() {
    return force;
  }

  @Override
  public String suffix() {
    return suffix;
  }

  @Override
  public String suffixToRemove() {
    return suffixToRemove;
  }

  // TODO: simple name and property name suffix
  public String applySuffix(String input) {
    return isNotBlank(suffixToRemove) ? input.replaceAll(suffixToRemove + "$", suffix) : input + suffix;
  }

  // TODO indicate somewhere in some way that in the end.. excludes preceded includes
  @Override
  public boolean toInclude(String field) {
    return !excludedFields.isEmpty() && includedFields.contains(field);
  }

  @Override
  public boolean toExclude(String field) {
    return excludedFields.contains(field);
  }

  @Override
  public File getFile() {
    return file;
  }

  @Override
  public String toString() {
    return format("\tClass: %s\n\tExcluded Fields: %s\n\tIncluded Fields: %s\n\tMandatory Fields: %s\n\tForce: %s",
        classCanonicalName, excludedFields, includedFields, mandatoryFields, force);
  }
}
