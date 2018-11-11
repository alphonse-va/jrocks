package jrocks.model;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.FieldApi;
import jrocks.plugin.util.Inflector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ReflectiveClassInfo implements ClassApi {

  private static final Inflector INFLECTOR = new Inflector();

  private Class<?> beanClass;

  private List<FieldApi> properties = new ArrayList<>();

  ReflectiveClassInfo(Class<?> beanClass) {
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
  public String pluralSimpleName() {
    return INFLECTOR.pluralize(simpleName());
  }

  @Override
  public String propertyName() {
    return Character.toLowerCase(simpleName().charAt(0)) + simpleName().substring(1);
  }

  @Override
  public String pluralPropertyName() {
    return INFLECTOR.pluralize(propertyName());
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
  public File getSourceClassPath() {
    return null;
  }
}
