package jrocks.model;

import jrocks.api.ClassInfoApi;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Objects;
import java.util.stream.Stream;

public class BeanMetaDataBuilder<T> {

  private ClassInfoApi beanMetaData;

  private Class<T> beanClass;

  public BeanMetaDataBuilder(Class<T> beanClass) {
    this.beanClass = beanClass;
    this.beanMetaData = new BeanClassInfo<>(beanClass);
  }

  public ClassInfoApi build() {
    Objects.requireNonNull(beanMetaData);
    Stream.of(FieldUtils.getAllFields(beanClass))
        .forEach(e -> beanMetaData.addProperty(new FieldClassInfo<>(e)));
    return beanMetaData;
  }
}
