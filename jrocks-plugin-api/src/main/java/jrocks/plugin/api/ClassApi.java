package jrocks.plugin.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public interface ClassApi {

  String packageName();

  String simpleName();

  String pluralSimpleName();

  String name();

  String propertyName();

  String pluralPropertyName();

  void addField(FieldApi field);

  boolean hasRequiredFields();

  default List<FieldApi> fields() {
    return new ArrayList<>();
  }

  default List<String> requiredFieldNames() {
    Objects.requireNonNull(fields(), "fields() should never returns null!");
    return fields().stream()
        .filter(FieldApi::isRequired)
        .map(ClassApi::name)
        .collect(Collectors.toList());
  }

  File sourceClassPath();
}
