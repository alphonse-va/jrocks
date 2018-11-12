package jrocks.shell.parameter;

import jrocks.plugin.api.PluginGenerator;
import jrocks.plugin.api.QuestionResponse;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.*;
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
  private List<String> additionalFlags;
  private Map<Object, QuestionResponse> responses;
  private PluginGenerator layout;
  private boolean dryRun;

  public BaseClassInfoParameterApi build() {
    Objects.requireNonNull(classCanonicalName, "classCanonicalName is required");
    return new BaseClassInfoParameterApi()
        .setClassCanonicalName(classCanonicalName)
        .setExcludedFields(excludedFields)
        .setIncludedFields(includedFields)
        .setMandatoryFields(mandatoryFields)
        .setSuffix(suffix)
        .setSuffixToRemove(suffixToRemove)
        .setFile(file)
        .setResponses(responses)
        .setAdditionalFlags(additionalFlags)
        .setLayout(layout)
        .setForce(force)
        .setDryRun(dryRun);
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
    this.additionalFlags = asList(addtionalFlags);
    return this;
  }

  public BaseClassInfoParameterBuilder withLayout(PluginGenerator layout) {
    this.layout = layout;
    return this;
  }

  public BaseClassInfoParameterBuilder withDriRun(boolean dryRun) {
    this.dryRun = dryRun;
    return this;
  }

  public BaseClassInfoParameterBuilder withAdditionalResponses(Map<Object, QuestionResponse> responses) {
    this.responses = responses;
    return this;
  }
}