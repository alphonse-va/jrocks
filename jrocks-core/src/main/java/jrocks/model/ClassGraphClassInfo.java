package jrocks.model;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.FieldApi;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ClassGraphClassInfo implements ClassApi {

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
  public List<FieldApi> fields() {
    return properties;
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
  public File sourceClassPath() {
    return classInfo.getClasspathElementFile();
  }
}
