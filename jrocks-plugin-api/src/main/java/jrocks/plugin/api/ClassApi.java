package jrocks.plugin.api;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public interface ClassApi {

  Inflector inflector = new Inflector();

  String packageName();

  String simpleName();

  String name();

  void addField(FieldApi field);

  boolean hasRequiredFields();

  default String pluralSimpleName() {
    return inflector.pluralize(simpleName());
  }

  default String propertyName() {
    return Character.toLowerCase(simpleName().charAt(0)) + simpleName().substring(1);
  }

  default String pluralPropertyName() {
    return inflector.pluralize(propertyName());
  }

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

  default String resourceName() {
    return simpleName().replaceAll("(.)(\\p{Upper})", "$1-$2").toLowerCase();
  }

  File sourceClassPath();

  /**
   * Returns e.g "id", "name", "desc"
   *
   * @return
   */
  default String fieldNamesAsCsvDoubleCote() {
    return fields().stream()
        .filter(f -> f.getter().isPresent() && f.setter().isPresent())
        .map(f -> "\"" + f.fieldName() + "\"")
        .collect(Collectors.joining(", "));
  }

  /**
   * Returns e.g id, name, desc
   *
   * @return
   */
  default String fieldNamesAsCsv() {
    return fields().stream()
        .filter(f -> f.getter().isPresent() && f.setter().isPresent())
        .map(FieldApi::fieldName)
        .collect(Collectors.joining(", "));
  }

  default Optional<FieldApi> idFieldOptional() {
    return fields().stream()
        .filter(f -> f.isAnnotatedWith("javax.persistence.Id"))
        .findAny();
  }

  /**
   * Returns the entity field annotated with <code>@Id</code>
   * <p>
   * orElseThrow IllegalArgumentException
   *
   * @return
   */
  default FieldApi idField() {
    return idFieldOptional()
        .orElseThrow(() -> new IllegalArgumentException("Class '" + name() + "' doesn't contain any field annotated with JPA @Id"));
  }
}
