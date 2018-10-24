package jrocks.model;

import java.io.File;
import java.util.List;

public interface ClassInfo {

  String packageName();

  String simpleName();

  String pluralSimpleName();

  String canonicalName();

  String propertyName();

  String pluralPropertyName();

  List<String> requiredFieldCanonicalNames();

  List<String> fieldCanonicalNames();

  List<FieldClassInfo> getFields();

  void addField(FieldClassInfo metaData);

  boolean hasRequiredFields();

  File getSourceClassPath();
}
