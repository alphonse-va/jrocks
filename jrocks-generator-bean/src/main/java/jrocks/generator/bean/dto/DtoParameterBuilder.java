package jrocks.generator.bean.dto;

import jrocks.model.ClassInfoParameter;

public class DtoParameterBuilder {

  private boolean withFactoryMethod;
  private ClassInfoParameter classInfoParameter;


  public DtoParameterBuilder withFactoryMethod(boolean withFactoryMethod) {
    this.withFactoryMethod = withFactoryMethod;
    return this;
  }

  public DtoParameterBuilder classInfoParameter(ClassInfoParameter classInfoParameter) {
    this.classInfoParameter = classInfoParameter;
    return this;
  }

  public DtoParameter build() {
    return new DtoParameter(classInfoParameter, withFactoryMethod);
  }
}
