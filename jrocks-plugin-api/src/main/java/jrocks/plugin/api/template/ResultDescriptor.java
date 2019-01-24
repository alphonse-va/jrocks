package jrocks.plugin.api.template;

import java.io.File;
import java.util.StringJoiner;

public class ResultDescriptor {

  private TemplateDescriptor templateDescriptor;

  private File file;

  private String content;

  private TemplateContext context;

  public TemplateDescriptor getTemplateDescriptor() {
    return templateDescriptor;
  }

  public ResultDescriptor setDescriptor(TemplateDescriptor sourceTemplate) {
    this.templateDescriptor = sourceTemplate;
    return this;
  }

  public File getFile() {
    return file;
  }

  public ResultDescriptor setFile(File file) {
    this.file = file;
    return this;
  }

  public String getContent() {
    return content;
  }

  public ResultDescriptor setContent(String content) {
    this.content = content;
    return this;
  }

  public ResultDescriptor setContext(TemplateContext context) {
    this.context = context;
    return this;
  }

  public TemplateContext getContext() {
    return context;
  }

  @Override
  public String toString() {
    return new StringJoiner("\n", "ResultDescriptor:\n", "")
        .add("\tfile: " + file)
        .add(content != null ? "\tcontent: '" + content.split("\n")[0] + " ...'" : "")
        .add(templateDescriptor.toString())
        .toString();
  }
}
