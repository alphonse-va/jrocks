package jrocks.model;

import jrocks.api.EntityClassInfoApi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityClassInfo extends AbstractClassInfoApi implements EntityClassInfoApi {

  private static final String REST_SEPARATOR = "-";
  private static final Pattern CAMELCASE_TO_REST_PATTERN = Pattern.compile("(?=[A-Z][a-z])");

  private Class<?> idClass;

  public EntityClassInfo(Class<?> beanClass, Class<?> idClass) {
    super(beanClass);
    this.idClass = idClass;
  }

  @Override
  public String idCanonicalName() {
    return idClass.getCanonicalName();
  }

  @Override
  public String idSimpleName() {
    return idClass.getSimpleName();
  }

  @Override
  public String restPath() {
    final Matcher matcher = CAMELCASE_TO_REST_PATTERN.matcher(propertyName());
    return matcher.replaceAll(REST_SEPARATOR).toLowerCase();
  }
}
