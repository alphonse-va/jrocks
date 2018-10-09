package jrocks.api;

public interface EntityClassInfoApi extends ClassInfoApi {

  String idCanonicalName();

  String idSimpleName();

  String restPath();
}
