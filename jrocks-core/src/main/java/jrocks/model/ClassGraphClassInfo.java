package jrocks.model;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.FieldApi;
import jrocks.plugin.util.Inflector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import static org.apache.commons.lang3.StringUtils.startsWith;

public class ClassGraphClassInfo implements ClassApi {

  private static final Inflector INFLECTOR = new Inflector();

  private io.github.classgraph.ClassInfo classInfo;

  private List<FieldApi> properties = new ArrayList<>();

  ClassGraphClassInfo(io.github.classgraph.ClassInfo classInfo) {
    Objects.requireNonNull(classInfo);
    this.classInfo = classInfo;
  }

  @Override
  public String packageName() {
    return classInfo.getName().substring(0, classInfo.getName().lastIndexOf("."));
  }

  @Override
  public String name() {
    return classInfo.getName();
  }

  @Override
  public String simpleName() {
    return classInfo.getSimpleName();
  }

  @Override
  public String pluralSimpleName() {
    return INFLECTOR.pluralize(name());
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

  public void setProperties(List<FieldApi> properties) {
    this.properties = properties;
  }

  @Override
  public void addField(FieldApi metaData) {
    properties.add(metaData);
  }

  @Override
  public boolean hasRequiredFields() {
    return properties.stream().anyMatch(FieldApi::isRequired);
  }

  @Override
  public File getSourceClassPath() {
    return classInfo.getClasspathElementFile();
  }

  private static Predicate<FieldApi> javaLangFilter() {
    return f -> !startsWith(f.name(), "java.lang");
  }
}
