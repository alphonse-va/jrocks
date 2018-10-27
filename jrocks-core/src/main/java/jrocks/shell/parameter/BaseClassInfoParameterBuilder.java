package jrocks.shell.parameter;

import java.io.File;
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
  private File file;
  private List<String> addtionalFlags;

  public BaseClassInfoParameter build() {
    Objects.requireNonNull(classCanonicalName, "classCanonicalName is required");
    return new BaseClassInfoParameter(classCanonicalName, force, excludedFields, includedFields, mandatoryFields, suffix, suffixToRemove, file, addtionalFlags);
  }

  public BaseClassInfoParameterBuilder withClassCanonicalName(String classCanonicalName) {
    this.classCanonicalName = classCanonicalName;
    return this;
  }

  public BaseClassInfoParameterBuilder withForce(boolean force) {
    this.force = force;
    return this;
  }

  public BaseClassInfoParameterBuilder withExcludedFields(String[] excludedFields) {
    this.excludedFields = asList(excludedFields);
    return this;
  }

  public BaseClassInfoParameterBuilder withIncludedFields(String[] includedFields) {
    this.includedFields = asList(includedFields);
    return this;
  }

  public BaseClassInfoParameterBuilder withMandatoryFields(String[] mandatoryFields) {
    this.mandatoryFields = asList(mandatoryFields);
    return this;
  }

  public BaseClassInfoParameterBuilder withSuffix(String suffix) {
    this.suffix = suffix;
    return this;
  }

  public BaseClassInfoParameterBuilder withSuffixToRemove(String suffixToRemove) {
    this.suffixToRemove = suffixToRemove;
    return this;
  }

  public BaseClassInfoParameterBuilder withFile(File file) {
    this.file = file;
    return this;
  }

  public BaseClassInfoParameterBuilder withAdditionalFlags(String[] addtionalFlags) {
    this.addtionalFlags = asList(addtionalFlags);
    return this;
  }
}