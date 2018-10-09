package jrocks.api;

import java.util.List;

public interface ClassInfoApi {

  String packageName();

  String simpleName();

  String pluralSimpleName();

  String canonicalName();

  String propertyName();

  String pluralPropertyName();

  List<String> requiredFieldCanonicalNames();

  List<String> fieldCanonicalNames();

  List<FieldClassInfoApi> getFields();

  void addProperty(FieldClassInfoApi metaData);
}
