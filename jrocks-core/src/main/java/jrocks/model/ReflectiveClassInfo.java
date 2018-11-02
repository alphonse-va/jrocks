package jrocks.model;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.FieldApi;
import jrocks.plugin.util.Inflector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.startsWith;

public class ReflectiveClassInfo implements ClassInfo {

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
    return INFLECTOR.pluralize(name());
  }

  @Override
  public String propertyName() {
    return Character.toLowerCase(name().charAt(0)) + name().substring(1);
  }

  @Override
  public String pluralPropertyName() {
    return INFLECTOR.pluralize(propertyName());
  }

  @Override
  public List<String> fieldCanonicalNames() {
    return properties.stream()
        .filter(javaLangFilter())
        .map(ClassApi::name).distinct().collect(Collectors.toList());
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

  private Predicate<FieldApi> javaLangFilter() {
    return f -> !startsWith(f.name(), "java.lang");
  }
}
