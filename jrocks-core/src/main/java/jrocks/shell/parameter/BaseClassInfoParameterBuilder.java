package jrocks.shell.parameter;

import jrocks.shell.LogLevel;

import java.util.List;
import java.util.Objects;

public class BaseClassInfoParameterBuilder {

  private String classCanonicalName;
  private LogLevel logLevel = LogLevel.info;
  private boolean force;
  private List<String> excludedFields;
  private List<String> includedFields;
  private List<String> mandatoryFields;

  public BaseClassInfoParameterBuilder setClassCanonicalName(final String classCanonicalName) {
    this.classCanonicalName = classCanonicalName;
    return this;
  }

  public BaseClassInfoParameterBuilder setLogLevel(final LogLevel logLevel) {
    this.logLevel = logLevel;
    return this;
  }

  public BaseClassInfoParameterBuilder setForce(final boolean force) {
    this.force = force;
    return this;
  }

  public BaseClassInfoParameterBuilder setExcludedFields(final List<String> excludedFields) {
    this.excludedFields = excludedFields;
    return this;
  }

  public BaseClassInfoParameterBuilder setIncludedFields(final List<String> includedFields) {
    this.includedFields = includedFields;
    return this;
  }

  public BaseClassInfoParameterBuilder setMandatoryFields(final List<String> mandatoryFields) {
    this.mandatoryFields = mandatoryFields;
    return this;
  }

  public BaseClassInfoParameter build() {
    Objects.requireNonNull(classCanonicalName, "classCanonicalName is required");
    return new BaseClassInfoParameter(classCanonicalName, logLevel, force, excludedFields, includedFields, mandatoryFields);
  }
}