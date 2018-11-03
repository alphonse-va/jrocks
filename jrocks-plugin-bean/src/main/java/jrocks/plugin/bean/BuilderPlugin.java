package jrocks.plugin.bean;

import jrocks.plugin.api.JRocksPlugin;
import jrocks.plugin.api.PluginLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class BuilderPlugin implements JRocksPlugin {

  public static final String LAYOUT_QUALIFIER = "BuilderLayout";

  private final List<PluginLayout> layouts;

  @Autowired
  public BuilderPlugin(@Qualifier(LAYOUT_QUALIFIER) List<PluginLayout> layouts) {
    this.layouts = layouts;
  }

  @Override
  public String name() {
    return "builder";
  }

  @Override
  public String description() {
    return "Generate a builder for selected class";
  }

  @Override
  public List<String> keys() {
    return Collections.singletonList("builder");
  }

  @Override
  public String defaultSuffix() {
    return "Builder";
  }

  @Override
  public List<PluginLayout> layouts() {
    return layouts;
  }

  @Override
  public String layoutQualifier() {
    return LAYOUT_QUALIFIER;
  }
}
