package jrocks.plugin.api;

import java.io.File;
import java.util.StringJoiner;

public class GeneratedSourceSupport implements GeneratedSource {

  private String content;
  private String packageName;
  private File path;
  private String filename;

  @Override
  public String content() {
    return content;
  }

  @Override
  public File path() {
    return path;
  }


  @Override
  public String filename() {
    return filename;
  }

  @Override
  public String packageName() {
    return packageName;
  }

  public GeneratedSourceSupport setContent(String content) {
    this.content = content;
    return this;
  }

  public GeneratedSourceSupport setPath(File path) {
    this.path = path;
    return this;
  }

  public GeneratedSourceSupport setPackageName(String packageName) {
    this.packageName = packageName;
    return this;
  }

  public GeneratedSourceSupport setFilename(String filename) {
    this.filename = filename;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", "source: ", "")
        .add("content: _" + content + "_")
        .add("package: _" + packageName + "_")
        .add("path: _" + path.getName() + "_")
        .toString();
  }
}
