package jrocks.shell.parameter;

import jrocks.model.ClassInfoParameter;
import jrocks.plugin.api.PluginLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.collections.CollectionUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class BaseClassInfoParameter implements ClassInfoParameter {

  private String classCanonicalName;

  private List<String> excludedFields;

  private List<String> includedFields;

  private List<String> mandatoryFields;

  private String suffix;

  private String suffixToRemove;

  private boolean force;

  private File file;

  private List<String> additionalFlags;

  private PluginLayout layout;

  BaseClassInfoParameter(String classCanonicalName,
                         boolean force,
                         List<String> excludedFields,
                         List<String> includedFields,
                         List<String> mandatoryFields,
                         String suffix,
                         String suffixToRemove, File file,
                         List<String> additionalFlags, PluginLayout layout) {
    this.classCanonicalName = classCanonicalName;
    this.force = force;
    this.excludedFields = excludedFields != null ? excludedFields : new ArrayList<>();
    this.includedFields = includedFields != null ? includedFields : new ArrayList<>();
    this.mandatoryFields = mandatoryFields != null ? mandatoryFields : new ArrayList<>();
    this.suffix = suffix;
    this.suffixToRemove = suffixToRemove;
    this.file = file;
    this.additionalFlags = additionalFlags;
    this.layout = layout;
  }

  protected BaseClassInfoParameter(ClassInfoParameter parameter) {
    this(parameter.classCanonicalName(),
        parameter.isForce(),
        parameter.excludedFields(),
        parameter.includedFields(),
        parameter.mandatoryFields(),
        parameter.suffix(),
        parameter.suffixToRemove(),
        parameter.getFile(),
        parameter.getAdditionalFlags(),
        parameter.getLayout());
  }

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
  public List<String> getAdditionalFlags() {
    return additionalFlags;
  }

  @Override
  public PluginLayout getLayout() {
    return layout;
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
