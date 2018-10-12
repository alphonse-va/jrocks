package jrocks.shell.command.buider;

import jrocks.shell.parameter.BeanClassInfoParameter;

import java.util.ArrayList;
import java.util.List;

public class BuilderParameter implements BeanClassInfoParameter {

  private Class<?> sourceClass;

  private List<String> excludedFields;

  private List<String> includedFields;

  private List<String> mandatoryFields;

  BuilderParameter(final Class<?> sourceClass, final List<String> excludedFields, final List<String> includedFields, final List<String> mandatoryFields) {
    this.sourceClass = sourceClass;
    this.excludedFields = excludedFields != null ? excludedFields : new ArrayList<>();
    this.includedFields = includedFields != null ? includedFields : new ArrayList<>();
    this.mandatoryFields = mandatoryFields != null ? mandatoryFields : new ArrayList<>();
  }

  @Override
  public Class<?> getSourceClass() {
    return sourceClass;
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
}
