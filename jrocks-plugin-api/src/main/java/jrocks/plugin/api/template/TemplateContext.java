package jrocks.plugin.api.template;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.ClassParameterApi;

import java.util.Map;
import java.util.StringJoiner;

public class TemplateContext {

  private ClassApi bean;

  private ClassParameterApi parameter;

  private Map<String, Object> pluginContext;

  public ClassApi bean() {
    return bean;
  }

  public TemplateContext setBean(ClassApi bean) {
    this.bean = bean;
    return this;
  }

  public ClassParameterApi parameter() {
    return parameter;
  }

  public TemplateContext setParameter(ClassParameterApi parameter) {
    this.parameter = parameter;
    return this;
  }

  public Map<String, Object> pluginContext() {
    return pluginContext;
  }

  public TemplateContext setPluginContext(Map<String, Object> pluginContext) {
    this.pluginContext = pluginContext;
    return this;
  }


  @Override
  public String toString() {
    return new StringJoiner("\n\t", "TemplateContext:\n\t", "")
        .add("bean: " + bean)
        .add("parameter: " + parameter)
        .add("pluginContext: " + pluginContext)
        .toString();
  }
}
