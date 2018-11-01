package jrocks.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PluginInfo {

  private File jarFile;

  private List<CommandInfo> commands;

  public PluginInfo addCommand(CommandInfo commandInfo) {
    if (commands == null) commands = new ArrayList<>();
    commands.add(commandInfo);
    return this;
  }

  public File getJarFile() {
    return jarFile;
  }

  public PluginInfo setJarFile(File jarFile) {
    this.jarFile = jarFile;
    return this;
  }

  public List<CommandInfo> getCommands() {
    return commands;
  }
}

