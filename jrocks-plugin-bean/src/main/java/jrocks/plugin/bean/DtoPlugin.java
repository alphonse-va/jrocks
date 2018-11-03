package jrocks.plugin.bean;

import jrocks.plugin.api.JRocksPlugin;
import jrocks.plugin.api.PluginLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DtoPlugin implements JRocksPlugin {

  private final List<PluginLayout> layouts;

  @Autowired
  public DtoPlugin(@Qualifier("DtoLayout") List<PluginLayout> layouts) {
    this.layouts = layouts;
  }

  @Override
  public String defaultSuffix() {
    return "DTO";
  }

  @Override
  public List<PluginLayout> layouts() {
    return layouts;
  }

  @Override
  public String name() {
    return "dto";
  }

  @Override
  public String description() {
    return "DTO Generator";
  }

  @Override
  public List<String> keys() {
    return Collections.singletonList("dto");
  }
}
