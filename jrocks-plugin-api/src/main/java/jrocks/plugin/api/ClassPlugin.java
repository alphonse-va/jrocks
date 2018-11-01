package jrocks.plugin.api;

import java.util.List;

public interface ClassPlugin {

  List<GeneratedSource> generateSources(ClassParameter parameter, ClassApi clazz) ;
}
