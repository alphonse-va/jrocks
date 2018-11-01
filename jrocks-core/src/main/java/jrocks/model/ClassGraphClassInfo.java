package jrocks.model;

import jrocks.util.Inflector;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.startsWith;

public class ClassGraphClassInfo implements ClassInfo {

  private static final Inflector INFLECTOR = new Inflector();

  private io.github.classgraph.ClassInfo classInfo;

  private List<FieldClassInfo> properties = new ArrayList<>();

  ClassGraphClassInfo(io.github.classgraph.ClassInfo classInfo) {
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
  public List<String> fieldCanonicalNames() {
    return properties.stream()
        .filter(javaLangFilter())
        .map(ClassInfo::canonicalName).distinct().collect(Collectors.toList());
  }

  @Override
  public List<FieldClassInfo> getFields() {
    return properties;
  }

  public void setProperties(List<FieldClassInfo> properties) {
    this.properties = properties;
  }

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
    return classInfo.getClasspathElementFile();
  }

  private static Predicate<FieldClassInfo> javaLangFilter() {
    return f -> !startsWith(f.canonicalName(), "java.lang");
  }
}
