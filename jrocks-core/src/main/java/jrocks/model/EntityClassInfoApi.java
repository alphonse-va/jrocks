package jrocks.model;

public interface EntityClassInfoApi extends ClassInfoApi {

  String idCanonicalName();

  String idSimpleName();

  String restPath();
}
