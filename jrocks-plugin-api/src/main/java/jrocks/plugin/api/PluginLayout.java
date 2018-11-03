package jrocks.plugin.api;

import java.util.List;

public interface PluginLayout {

  String name();

  List<GeneratedSource> generate(ClassParameter parameter, ClassApi classApi);
}
