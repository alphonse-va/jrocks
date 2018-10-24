package jrocks.shell.generator.dto;

import jrocks.model.ClassInfoParameter;
import jrocks.shell.parameter.BaseClassInfoParameter;

public class DtoParameter extends BaseClassInfoParameter {

  private boolean withFactoryMethod;

  DtoParameter(ClassInfoParameter parameter, boolean withFactoryMethod) {
    super(parameter);
    this.withFactoryMethod = withFactoryMethod;
  }

  public boolean withFactoryMethod() {
    return withFactoryMethod;
  }
}
