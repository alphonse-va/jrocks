package jrocks.model;

import jrocks.api.ClassInfoApi;
import jrocks.api.FieldClassInfoApi;
import jrocks.template.util.Inflector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.startsWith;

public class BaseClassInfo implements ClassInfoApi {

  private static final Inflector INFLECTOR = new Inflector();

  private Class<?> beanClass;

  private List<FieldClassInfoApi> properties = new ArrayList<>();

  BaseClassInfo(Class<?> beanClass) {
    this.beanClass = beanClass;
  }

  @Override
  public String packageName() {
    return beanClass.getPackage().getName();
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
  public String canonicalName() {
    return beanClass.getCanonicalName();
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
  public List<String> requiredFieldCanonicalNames() {
    return properties.stream()
        .filter(FieldClassInfoApi::isRequired)
        .filter(javaLangFilter())
        .map(ClassInfoApi::canonicalName).distinct().collect(Collectors.toList());
  }

  @Override
  public List<String> fieldCanonicalNames() {
    return properties.stream()
        .filter(javaLangFilter())
        .map(ClassInfoApi::canonicalName).distinct().collect(Collectors.toList());
  }

  @Override
  public List<FieldClassInfoApi> getFields() {
    return properties;
  }

  public void setProperties(List<FieldClassInfoApi> properties) {
    this.properties = properties;
  }

  @Override
  public void addProperty(FieldClassInfoApi metaData) {
    properties.add(metaData);
  }

  @Override
  public boolean hasRequiredFields() {
    return properties.stream().anyMatch(FieldClassInfoApi::isRequired);
  }

  private static Predicate<FieldClassInfoApi> javaLangFilter() {
    return f -> !startsWith(f.canonicalName(), "java.lang");
  }

  @Override
  public Class<?> getBeanClass() {
    return beanClass;
  }
}
