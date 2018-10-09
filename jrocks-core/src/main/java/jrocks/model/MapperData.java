package jrocks.model;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class MapperData {

  private boolean withFactoryMethod;
  private String suffix;
  private String suffixToRemove;

  // TODO: simple name and property name suffix

  public String applySuffix(String input) {
    return isNotBlank(suffixToRemove) ? input.replaceAll(suffixToRemove + "$", suffix) : input + suffix;
  }

  public String suffix() {
    return suffix;
  }

  public MapperData setSuffix(String suffix) {
    this.suffix = suffix;
    return this;
  }

  public boolean withFactoryMethod() {
    return withFactoryMethod;
  }

  public MapperData setWithFactoryMethod(boolean withFactoryMethod) {
    this.withFactoryMethod = withFactoryMethod;
    return this;
  }

  public String getSuffixToRemove() {
    return suffixToRemove;
  }

  public MapperData setSuffixToRemove(String suffixToRemove) {
    this.suffixToRemove = suffixToRemove;
    return this;
  }
}
