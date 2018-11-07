package jrocks.shell.parameter;

import jrocks.plugin.api.PluginLayout;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
  private PluginLayout layout;
  private boolean dryRun;

  public BaseClassInfoParameterApi build() {
    Objects.requireNonNull(classCanonicalName, "classCanonicalName is required");
    return new BaseClassInfoParameterApi(classCanonicalName, force, excludedFields, includedFields, mandatoryFields, suffix, suffixToRemove, file, addtionalFlags, layout, dryRun);
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
    this.excludedFields = Arrays.stream(excludedFields)
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.toList());
    return this;
  }

  public BaseClassInfoParameterBuilder withIncludedFields(String[] includedFields) {
    this.includedFields = Arrays.stream(includedFields).filter(StringUtils::isNotBlank)
        .collect(Collectors.toList());
    return this;
  }

  public BaseClassInfoParameterBuilder withMandatoryFields(String[] mandatoryFields) {
    this.mandatoryFields = Arrays.stream(mandatoryFields)
        .filter(StringUtils::isNotBlank)
        .collect(Collectors.toList());
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

  public BaseClassInfoParameterBuilder withLayout(PluginLayout layout) {
    this.layout = layout;
    return this;
  }

  public BaseClassInfoParameterBuilder withDriRun(boolean dryRun) {
    this.dryRun = dryRun;
    return this;
  }
}