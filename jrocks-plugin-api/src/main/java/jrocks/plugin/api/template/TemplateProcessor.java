package jrocks.plugin.api.template;

import java.io.File;
import java.util.List;

public interface TemplateProcessor {

  List<ResultDescriptor> process(TemplateContext context, File pluginConfigFile);
}
