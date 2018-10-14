package jrocks.model;

import jrocks.api.ClassInfoApi;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Objects;
import java.util.stream.Stream;

public class BaseClassInfoBuilder {

  private ClassInfoApi beanMetaData;

  private Class<?> beanClass;

  public BaseClassInfoBuilder(Class<?> beanClass) {
    this.beanClass = beanClass;
    this.beanMetaData = new BaseClassInfo(beanClass);
  }

  public ClassInfoApi build() {
    Objects.requireNonNull(beanMetaData);
    Stream.of(FieldUtils.getAllFields(beanClass))
        .forEach(e -> beanMetaData.addProperty(new FieldClassInfo(e)));
    return beanMetaData;
  }
}