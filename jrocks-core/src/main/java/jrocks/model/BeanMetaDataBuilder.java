package jrocks.model;

import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Objects;
import java.util.stream.Stream;

public class BeanMetaDataBuilder<T> {

  private BeanMetaData<T> beanMetaData;

  public BeanMetaDataBuilder(Class<T> beanClass) {
    this.beanMetaData = new BeanMetaData<>(beanClass);
  }

  public BeanMetaData<T> build() {
    Objects.requireNonNull(beanMetaData);
    Stream.of(FieldUtils.getAllFields(beanMetaData.getBeanClass()))
        .forEach(e -> beanMetaData.addProperty(new FieldMetaData<>(e)));
    return beanMetaData;
  }
}
