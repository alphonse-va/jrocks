package jrocks.model;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.FieldApi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReflectiveClassInfo implements ClassApi {


  private Class<?> beanClass;

  private List<FieldApi> properties = new ArrayList<>();

  public ReflectiveClassInfo(Class<?> beanClass) {
    this.beanClass = beanClass;
  }

  @Override
  public String packageName() {
    return beanClass.getPackage().getName();
  }

  @Override
  public String name() {
    return beanClass.getCanonicalName();
  }

  @Override
  public String simpleName() {
    return beanClass.getSimpleName();
  }

  @Override
  public List<FieldApi> fields() {
    return properties;
  }

  @Override
  public void addField(FieldApi field) {
    properties.add(field);
  }

  @Override
  public boolean hasRequiredFields() {
    return properties.stream().anyMatch(FieldApi::isRequired);
  }

  @Override
  public File sourceClassPath() {
    return null;
  }
}
