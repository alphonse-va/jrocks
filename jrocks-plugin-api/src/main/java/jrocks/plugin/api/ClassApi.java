package jrocks.plugin.api;

import java.io.File;
import java.util.List;

public interface ClassApi {

  String packageName();

  String simpleName();

  String pluralSimpleName();

  String name();

  String propertyName();

  String pluralPropertyName();

  List<FieldApi> fields();

  void addField(FieldApi field);

  boolean hasRequiredFields();

  File sourceClassPath();
}
