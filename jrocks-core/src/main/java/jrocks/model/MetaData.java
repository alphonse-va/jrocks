package jrocks.model;

import java.util.List;

public interface MetaData<T> {

  String packageName();

  String simpleName();

  String pluralSimpleName();

  String canonicalName();

  String propertyName();

  String pluralPropertyName();

  List<String> requiredFieldCanonicalNames();

  List<String> fieldCanonicalNames();

  Class<T> getBeanClass();

  List<FieldMetaData<?>> getFields();
}
