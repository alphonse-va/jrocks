package jrocks.plugin.api.template;

import java.nio.file.Path;
import java.util.StringJoiner;

public class TemplateDescriptor {

  private String sourcePath;
  private Path postWriteScript;

  private String targetPathExpression;
  private String targetFilenameExpression;
  private String templateExpression;
  private String postWriteExpression;
  public boolean isRocker() {
    return sourcePath.endsWith(".rocker.raw")
        || sourcePath.endsWith(".rocker.html");
  }

  public String getSourcePath() {
    return sourcePath;
  }

  public TemplateDescriptor setSourcePath(String sourcePath) {
    this.sourcePath = sourcePath;
    return this;
  }

  public Path getPostWriteScript() {
    return postWriteScript;
  }

  public TemplateDescriptor setPostWriteScript(Path postWriteScript) {
    this.postWriteScript = postWriteScript;
    return this;
  }

  public String getTargetPathExpression() {
    return targetPathExpression;
  }

  public TemplateDescriptor setTargetPathExpression(String targetPathExpression) {
    this.targetPathExpression = targetPathExpression;
    return this;
  }

  public String getTargetFilenameExpression() {
    return targetFilenameExpression;
  }

  public TemplateDescriptor setTargetFilenameExpression(String targetFilenameExpression) {
    this.targetFilenameExpression = targetFilenameExpression;
    return this;
  }

  public String getTemplateExpression() {
    return templateExpression;
  }

  public TemplateDescriptor setTemplateExpression(String templateExpression) {
    this.templateExpression = templateExpression;
    return this;
  }

  public String getPostWriteExpression() {
    return postWriteExpression;
  }

  public TemplateDescriptor setPostWriteExpression(String postWriteExpression) {
    this.postWriteExpression = postWriteExpression;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner("\n\t", "Template:\n\t", "")
        .add("sourcePath: '" + sourcePath + "'")
        .add("postWriteScript: " + postWriteScript)
        .add("targetPathExpression: '" + targetPathExpression + "'")
        .add("targetFilenameExpression: '" + targetFilenameExpression + "'")
        .add("templateExpression: '" + templateExpression + "'")
        .add("postWriteExpression: " + postWriteExpression)
        .toString();
  }
}
