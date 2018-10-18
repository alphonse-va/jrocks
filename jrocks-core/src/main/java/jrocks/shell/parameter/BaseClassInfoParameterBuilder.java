package jrocks.shell.parameter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Arrays.asList;

public class BaseClassInfoParameterBuilder {

  private String classCanonicalName;
  private boolean force;
  private List<String> excludedFields = new ArrayList<>();
  private List<String> includedFields = new ArrayList<>();
  private List<String> mandatoryFields = new ArrayList<>();
  private String suffix;
  private String suffixToRemove;

  public BaseClassInfoParameterBuilder setClassCanonicalName(String classCanonicalName) {
    this.classCanonicalName = classCanonicalName;
    return this;
  }

  public BaseClassInfoParameterBuilder setForce(boolean force) {
    this.force = force;
    return this;
  }

  public BaseClassInfoParameterBuilder setExcludedFields(String[] excludedFields) {
    this.excludedFields = asList(excludedFields);
    return this;
  }

  public BaseClassInfoParameterBuilder setIncludedFields(String[] includedFields) {
    this.includedFields = asList(includedFields);
    return this;
  }

  public BaseClassInfoParameterBuilder setMandatoryFields(String[] mandatoryFields) {
    this.mandatoryFields = asList(mandatoryFields);
    return this;
  }

  public BaseClassInfoParameterBuilder setSuffix(String suffix) {
    this.suffix = suffix;
    return this;
  }

  public BaseClassInfoParameterBuilder setSuffixToRemove(String suffixToRemove) {
    this.suffixToRemove = suffixToRemove;
    return this;
  }

  public BaseClassInfoParameter build() {
    Objects.requireNonNull(classCanonicalName, "classCanonicalName is required");
    return new BaseClassInfoParameter(classCanonicalName, force, excludedFields, includedFields, mandatoryFields, suffix, suffixToRemove);
  }
}