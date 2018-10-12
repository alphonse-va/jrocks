package jrocks.shell.parameter;

import jrocks.api.ClassInfoParameterApi;
import jrocks.shell.LogLevel;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class BaseClassInfoParameterApi implements ClassInfoParameterApi {

  private String classCanonicalName;

  private LogLevel logLevel;

  private List<String> excludedFields;

  private List<String> includedFields;

  private List<String> mandatoryFields;

  private boolean force;

  BaseClassInfoParameterApi(final String classCanonicalName,
                            final LogLevel logLevel,
                            final boolean force,
                            final List<String> excludedFields,
                            final List<String> includedFields,
                            final List<String> mandatoryFields) {
    this.logLevel = logLevel;
    this.classCanonicalName = classCanonicalName;
    this.force = force;
    this.excludedFields = excludedFields != null ? excludedFields : new ArrayList<>();
    this.includedFields = includedFields != null ? includedFields : new ArrayList<>();
    this.mandatoryFields = mandatoryFields != null ? mandatoryFields : new ArrayList<>();
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
  public LogLevel getLogLevel() {
    return logLevel;
  }

  @Override
  public boolean isForce() {
    return force;
  }

  @Override
  public String toString() {
    return format("\tClass: %s\n\tExcluded Fields: %s\n\tIncluded Fields: %s\n\tMandatory Fields: %s\n\tForce: %s",
        classCanonicalName, excludedFields, includedFields, mandatoryFields, force);
  }
}
