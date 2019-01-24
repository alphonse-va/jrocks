package jrocks.template;

import jrocks.plugin.api.template.TemplateDescriptor;

import java.util.List;

class TemplateConfigFile {

  private List<TemplateDescriptor> templates;

  List<TemplateDescriptor> getTemplates() {
    return templates;
  }

  public TemplateConfigFile setTemplates(List<TemplateDescriptor> templates) {
    this.templates = templates;
    return this;
  }
}
