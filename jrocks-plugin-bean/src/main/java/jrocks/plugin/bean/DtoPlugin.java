package jrocks.plugin.bean;

import jrocks.plugin.api.JRocksPlugin;
import jrocks.plugin.api.PluginGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DtoPlugin implements JRocksPlugin {

  static final String LAYOUT_QUALIFIER = "DtoLayout";

  @Value("${jrocks.version}")
  private String version;

  private final List<PluginGenerator> layouts;

  @Autowired
  public DtoPlugin(@Qualifier(LAYOUT_QUALIFIER) List<PluginGenerator> layouts) {
    this.layouts = layouts;
  }

  @Override
  public String defaultSuffix() {
    return "DTO";
  }

  @Override
  public String name() {
    return "dto";
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public String description() {
    return "DTO Generator";
  }

  @Override
  public List<String> keys() {
    return Collections.singletonList("dto");
  }

  @Override
  public List<PluginGenerator> generators() {
    return layouts;
  }

  @Override
  public String layoutQualifier() {
    return LAYOUT_QUALIFIER;
  }
}
