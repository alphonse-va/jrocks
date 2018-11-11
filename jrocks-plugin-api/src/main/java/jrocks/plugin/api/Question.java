package jrocks.plugin.api;

import java.util.List;

public interface Question {

  String text();

  String buffer();

  Character mask();

  List<String> proposals();
}
