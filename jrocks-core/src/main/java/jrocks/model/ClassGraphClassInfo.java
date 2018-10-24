package jrocks.model;

import io.github.classgraph.ClassInfo;
import jrocks.template.util.Inflector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.startsWith;

public class ClassGraphClassInfo implements ClassInfoApi {

  private static final Inflector INFLECTOR = new Inflector();

  private ClassInfo classInfo;

  private List<FieldClassInfoApi> properties = new ArrayList<>();

  ClassGraphClassInfo(ClassInfo classInfo) {
    Objects.requireNonNull(classInfo);
    this.classInfo = classInfo;
  }

  @Override
  public String packageName() {
    return classInfo.getName().substring(0, classInfo.getName().lastIndexOf("."));
  }

  @Override
  public String simpleName() {
    return classInfo.getSimpleName();
  }

  @Override
  public String pluralSimpleName() {
    return INFLECTOR.pluralize(simpleName());
  }

  @Override
  public String canonicalName() {
    return classInfo.getName();
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
  public void addField(FieldClassInfoApi metaData) {
    properties.add(metaData);
  }

  @Override
  public boolean hasRequiredFields() {
    return properties.stream().anyMatch(FieldClassInfoApi::isRequired);
  }

  @Override
  public File getSourceClassPath() {
    return classInfo.getClasspathElementFile();
  }

  private static Predicate<FieldClassInfoApi> javaLangFilter() {
    return f -> !startsWith(f.canonicalName(), "java.lang");
  }
}
