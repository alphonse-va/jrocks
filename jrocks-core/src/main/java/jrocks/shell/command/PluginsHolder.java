package jrocks.shell.command;

import jrocks.plugin.api.JRocksPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PluginsHolder {

  private final List<JRocksPlugin> plugins;

  private JRocksPlugin currentPlugin;

  @Autowired(required = false)
  public PluginsHolder(List<JRocksPlugin> plugins) {this.plugins = plugins;}

  public void initPlugin(String name) {
    if (plugins == null) return;
    currentPlugin = plugins.stream().filter(c -> c.name().equals(name))
        .findAny()
        .orElse(null);
  }

  public JRocksPlugin getCurrentPlugin() {
    return currentPlugin;
  }

  public List<String> getPluginNames() {
    return plugins == null
        ? new ArrayList<>()
        : plugins.stream().map(JRocksPlugin::name)
        .collect(Collectors.toList());
  }

  public List<JRocksPlugin> getPlugins() {
    return plugins == null
        ? new ArrayList<>()
        : plugins;
  }
}
