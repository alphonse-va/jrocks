package jrocks.shell.generator.dto;

import jrocks.model.ClassInfoParameterApi;
import jrocks.shell.parameter.BaseClassInfoParameter;

public class DtoParameter extends BaseClassInfoParameter {

  private boolean withFactoryMethod;

  DtoParameter(ClassInfoParameterApi parameter, boolean withFactoryMethod) {
    super(parameter);
    this.withFactoryMethod = withFactoryMethod;
  }

  public boolean withFactoryMethod() {
    return withFactoryMethod;
  }
}
