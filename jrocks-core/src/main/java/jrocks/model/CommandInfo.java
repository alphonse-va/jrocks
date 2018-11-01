package jrocks.model;

public class CommandInfo {

  private String key;
  private String description;
  private String group;

  public CommandInfo(String key, String description, String group) {
    this.key = key;
    this.description = description;
    this.group = group;
  }

  public String getKey() {
    return key;
  }

  public String getDescription() {
    return description;
  }

  public String getGroup() {
    return group;
  }
}
