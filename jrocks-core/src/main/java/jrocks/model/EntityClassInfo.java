package jrocks.model;

import io.github.classgraph.ClassInfo;
import jrocks.api.EntityClassInfoApi;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EntityClassInfo extends ClassGraphClassInfo implements EntityClassInfoApi {

  private static final String REST_SEPARATOR = "-";
  private static final Pattern CAMELCASE_TO_REST_PATTERN = Pattern.compile("(?=[A-Z][a-z])");

  private ReflectiveClassInfo idClass;

  public EntityClassInfo(ClassInfo beanClass, ReflectiveClassInfo idClass) {
    super(beanClass);
    this.idClass = idClass;
  }

  @Override
  public String idCanonicalName() {
    return idClass.canonicalName();
  }

  @Override
  public String idSimpleName() {
    return idClass.simpleName();
  }

  @Override
  public String restPath() {
    Matcher matcher = CAMELCASE_TO_REST_PATTERN.matcher(propertyName());
    return matcher.replaceAll(REST_SEPARATOR).toLowerCase();
  }
}
