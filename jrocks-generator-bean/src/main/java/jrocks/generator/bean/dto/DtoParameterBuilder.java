package jrocks.generator.bean.dto;

import jrocks.model.ClassInfoParameter;

public class DtoParameterBuilder {

  private boolean withFactoryMethod;
  private ClassInfoParameter classInfoParameter;


  DtoParameterBuilder setWithFactoryMethod(boolean withFactoryMethod) {
    this.withFactoryMethod = withFactoryMethod;
    return this;
  }

  public DtoParameterBuilder setClassInfoParameter(ClassInfoParameter classInfoParameter) {
    this.classInfoParameter = classInfoParameter;
    return this;
  }

  public DtoParameter build() {
    return new DtoParameter(classInfoParameter, withFactoryMethod);
  }
}
