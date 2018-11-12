package jrocks.shell.parameter;

import jrocks.plugin.api.ClassParameterApi;
import jrocks.plugin.api.PluginGenerator;
import jrocks.plugin.api.QuestionResponse;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.lang.String.format;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BaseClassInfoParameterApi implements ClassParameterApi {

  private boolean dryRun;
  private String classCanonicalName;
  private List<String> excludedFields;
  private List<String> includedFields;
  private List<String> mandatoryFields;
  private String suffix;
  private String suffixToRemove;
  private boolean force;
  private File file;
  private Map<Object, QuestionResponse> responses;
  private List<String> additionalFlags;

  private PluginGenerator layout;


  @Override
  public List<String> excludedFields() {
    return excludedFields;
  }

  @Override
  public List<String> includedFields() {
    return includedFields;
  }

  @Override
  public List<String> mandatoryFields() {
    return mandatoryFields;
  }

  @Override
  public String classCanonicalName() {
    return classCanonicalName;
  }

  @Override
  public boolean isForce() {
    return force;
  }

  @Override
  public boolean isDryRun() {
    return dryRun;
  }

  @Override
  public String suffix() {
    return suffix;
  }

  @Override
  public String suffixToRemove() {
    return suffixToRemove;
  }

  // TODO: simple name and property name defaultSuffix
  public String applySuffix(String input) {
    return isNotBlank(suffixToRemove) ? input.replaceAll(suffixToRemove, suffix) : input + suffix;
  }

  // TODO indicate that in the end.. excludes preceded includes
  @Override
  public boolean toInclude(String field) {
    return !excludedFields.isEmpty() || includedFields.contains(field) || includedFields.isEmpty();
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
  public Map<Object, QuestionResponse> responses() {
    return responses;
  }

  @Override
  public Optional<QuestionResponse> getUserResponse(Object key) {
    return Optional.ofNullable(responses.get(key));
  }

  @Override
  public List<String> additionalFlags() {
    return additionalFlags;
  }

  @Override
  public boolean hasFlag(String flag) {
    return additionalFlags.stream().anyMatch(f -> f.equals(flag));
  }

  @Override
  public void addResponses(Map<Object, QuestionResponse> responses) {
    this.responses = responses;
  }

  @Override
  public PluginGenerator getLayout() {
    return layout;
  }

  BaseClassInfoParameterApi setDryRun(boolean dryRun) {
    this.dryRun = dryRun;
    return this;
  }

  BaseClassInfoParameterApi setClassCanonicalName(String classCanonicalName) {
    this.classCanonicalName = classCanonicalName;
    return this;
  }

  BaseClassInfoParameterApi setExcludedFields(List<String> excludedFields) {
    this.excludedFields = excludedFields;
    return this;
  }

  BaseClassInfoParameterApi setIncludedFields(List<String> includedFields) {
    this.includedFields = includedFields;
    return this;
  }

  BaseClassInfoParameterApi setMandatoryFields(List<String> mandatoryFields) {
    this.mandatoryFields = mandatoryFields;
    return this;
  }

  BaseClassInfoParameterApi setSuffix(String suffix) {
    this.suffix = suffix;
    return this;
  }

  BaseClassInfoParameterApi setSuffixToRemove(String suffixToRemove) {
    this.suffixToRemove = suffixToRemove;
    return this;
  }

  BaseClassInfoParameterApi setForce(boolean force) {
    this.force = force;
    return this;
  }

  public BaseClassInfoParameterApi setFile(File file) {
    this.file = file;
    return this;
  }

  BaseClassInfoParameterApi setResponses(Map<Object, QuestionResponse> responses) {
    this.responses = responses;
    return this;
  }

  BaseClassInfoParameterApi setAdditionalFlags(List<String> additionalFlags) {
    this.additionalFlags = additionalFlags;
    return this;
  }

  BaseClassInfoParameterApi setLayout(PluginGenerator layout) {
    this.layout = layout;
    return this;
  }

  @Override
  public String toString() {
    StringBuilder result = new StringBuilder();
    if (isNotEmpty(excludedFields)) result.append(format("\n\texcluded Fields: _%s_", excludedFields));
    if (isNotEmpty(includedFields)) result.append(format("\n\tincluded Fields: _%s_", includedFields));
    if (isNotEmpty(mandatoryFields)) result.append(format("\n\tmandatory Fields: _%s_", mandatoryFields));
    result.append(format("\n\tforce: _%s_", force));
    result.append(format("\n\tsuffix: _%s_", suffix));
    result.append(format("\n\tsuffix to remove: _%s_", suffixToRemove));
    return result.toString();
  }
}
