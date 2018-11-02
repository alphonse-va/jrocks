package jrocks.model;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.FieldApi;

import java.io.File;
import java.util.List;

public interface ClassInfo extends ClassApi {

  String packageName();

  String name();

  String pluralSimpleName();

  String propertyName();

  String pluralPropertyName();

  List<String> fieldCanonicalNames();

  List<FieldApi> fields();

  void addField(FieldApi metaData);

  boolean hasRequiredFields();

  File getSourceClassPath();
}
