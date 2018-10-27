package jrocks.model;

import jrocks.util.Inflector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.startsWith;

public class ReflectiveClassInfo implements ClassInfo {

  private static final Inflector INFLECTOR = new Inflector();

  private Class<?> beanClass;

  private List<FieldClassInfo> properties = new ArrayList<>();

  public ReflectiveClassInfo(Class<?> beanClass) {
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
        .filter(FieldClassInfo::isRequired)
        .filter(javaLangFilter())
        .map(ClassInfo::canonicalName).distinct().collect(Collectors.toList());
  }

  @Override
  public List<String> fieldCanonicalNames() {
    return properties.stream()
        .filter(javaLangFilter())
        .map(ClassInfo::canonicalName).distinct().collect(Collectors.toList());
  }

  // getters

  @Override
  public List<FieldClassInfo> getFields() {
    return properties;
  }

  public void setProperties(List<FieldClassInfo> properties) {
    this.properties = properties;
  }

  // internals

  @Override
  public void addField(FieldClassInfo metaData) {
    properties.add(metaData);
  }

  @Override
  public boolean hasRequiredFields() {
    return properties.stream().anyMatch(FieldClassInfo::isRequired);
  }

  @Override
  public File getSourceClassPath() {
    return null;
  }

  private Predicate<FieldClassInfo> javaLangFilter() {
    return f -> !startsWith(f.canonicalName(), "java.lang");
  }
}
