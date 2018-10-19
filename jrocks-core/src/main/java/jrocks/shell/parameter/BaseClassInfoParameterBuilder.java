package jrocks.shell.parameter;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static io.github.classgraph.utils.ReflectionUtils.classForNameOrNull;
import static java.lang.String.format;
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
  private Class<?> sourceClass;

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
    sourceClass = classForNameOrNull(classCanonicalName);
    if (sourceClass == null) {
      throw new IllegalStateException(format("Class '%s' not found on the class path", classCanonicalName));
    }
    file = new File(sourceClass.getProtectionDomain().getCodeSource().getLocation().getPath());
    return new BaseClassInfoParameter(classCanonicalName, force, excludedFields, includedFields, mandatoryFields, suffix, suffixToRemove, file);
  }

  public BaseClassInfoParameterBuilder setFile(final File file) {
    this.file = file;
    return this;
  }
}