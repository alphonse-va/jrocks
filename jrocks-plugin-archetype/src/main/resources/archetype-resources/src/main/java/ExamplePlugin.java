#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import jrocks.plugin.api.JRocksPlugin;
import jrocks.plugin.api.PluginLayout;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class ExamplePlugin implements JRocksPlugin {

  public static final String LAYOUT_QUALIFIER = "ExampleLayout";

  private final List<PluginLayout> layouts;

  @Autowired
  public ExamplePlugin(@Qualifier(LAYOUT_QUALIFIER) List<PluginLayout> layouts) {
    this.layouts = layouts;
  }

  @Override
  public String defaultSuffix() {
    return "Example";
  }

  @Override
  public String name() {
    return "example";
  }

  @Override
  public String description() {
    return "Example Generator";
  }

  @Override
  public List<String> keys() {
    return Collections.singletonList("example");
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
