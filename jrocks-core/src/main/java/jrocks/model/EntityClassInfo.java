package jrocks.model;

public interface EntityClassInfo extends ClassInfo {

  String idCanonicalName();

  String idSimpleName();

  String restPath();
}
