package jrocks.shell.command.bean;

import jrocks.api.ClassInfoParameterApi;
import jrocks.shell.parameter.BaseClassInfoParameter;

public class DtoParameter extends BaseClassInfoParameter {

  private boolean withFactoryMethod;

  DtoParameter(final ClassInfoParameterApi parameter, final boolean withFactoryMethod) {
    super(parameter);
    this.withFactoryMethod = withFactoryMethod;
  }


  public boolean withFactoryMethod() {
    return withFactoryMethod;
  }
}
