package jrocks.plugin.api;

import java.io.File;

public class GeneratedSourceSupport implements GeneratedSource {

  private String content;
  private File path;

  @Override
  public String content() {
    return content;
  }

  @Override
  public File path() {
    return path;
  }

  public GeneratedSourceSupport setContent(String content) {
    this.content = content;
    return this;
  }

  public GeneratedSourceSupport setPath(File path) {
    this.path = path;
    return this;
  }
}
