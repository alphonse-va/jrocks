package jrocks.shell.command.bean;

import jrocks.api.ClassInfoParameterApi;

public class DtoParameterBuilder {

  private boolean withFactoryMethod;
  private ClassInfoParameterApi classInfoParameter;


  public DtoParameterBuilder setWithFactoryMethod(final boolean withFactoryMethod) {
    this.withFactoryMethod = withFactoryMethod;
    return this;
  }

  public DtoParameterBuilder setClassInfoParameter(final ClassInfoParameterApi classInfoParameter) {
    this.classInfoParameter = classInfoParameter;
    return this;
  }

  public DtoParameter build() {
    return new DtoParameter(classInfoParameter, withFactoryMethod);
  }
}
