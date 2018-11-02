package jrocks.shell.command;

import jrocks.plugin.api.JRocksPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurrentPluginHolder {

  private final List<JRocksPlugin> generatorCommands;

  private JRocksPlugin currentCommand;

  @Autowired(required = false)
  public CurrentPluginHolder(List<JRocksPlugin> generatorCommands) {this.generatorCommands = generatorCommands;}

  public void setCurrentCommand(String name) {
    if (generatorCommands == null) return;
    currentCommand = generatorCommands.stream().filter(c -> c.name().equals(name))
        .findAny()
        .orElse(null);
  }

  public JRocksPlugin getCurrentCommand() {
    return currentCommand;
  }

  public List<String> getPluginNames() {
    return generatorCommands == null
        ? new ArrayList<>()
        : generatorCommands.stream().map(JRocksPlugin::name)
        .collect(Collectors.toList());
  }
}
