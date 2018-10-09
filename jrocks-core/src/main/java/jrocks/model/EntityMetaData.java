package jrocks.model;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityMetaData<T, ID> extends AbstractMetaData<T> {

  private static final String REST_SEPARATOR = "-";
  private static final Pattern CAMELCASE_TO_REST_PATTERN = Pattern.compile("(?=[A-Z][a-z])");

  private Class<ID> idClass;

  public EntityMetaData(Class<T> beanClass, Class<ID> idClass) {
    super(beanClass);
    this.idClass = idClass;
  }

  public String idCanonicalName() {
    return idClass.getCanonicalName();
  }

  public String idSimpleName() {
    return idClass.getSimpleName();
  }

  public String restPath() {
    final Matcher matcher = CAMELCASE_TO_REST_PATTERN.matcher(propertyName());
    return matcher.replaceAll(REST_SEPARATOR).toLowerCase();
  }

  // getters and setters

  public Class<?> getIdClass() {
    return idClass;
  }
}
