package jrocks.plugin.springular;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.ClassParameterApi;
import jrocks.plugin.api.GeneratedSource;
import jrocks.plugin.api.PluginGenerator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Qualifier(SpringularPlugin.LAYOUT_QUALIFIER)
public class SpringularGenerator implements PluginGenerator {

  @Value("${jrocks.version}")
  private String version;

  @Override
  public String description() {
    return "Springular generator";
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public List<GeneratedSource> generate(ClassParameterApi parameter, ClassApi classApi) {
    return new ArrayList<>();
  }
}
