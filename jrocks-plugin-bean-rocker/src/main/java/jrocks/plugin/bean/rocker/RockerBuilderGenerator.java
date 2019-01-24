package jrocks.plugin.bean.rocker;

import com.fizzed.rocker.Rocker;
import jrocks.plugin.api.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Component
@Qualifier(RockerBuilderPlugin.LAYOUT_QUALIFIER)
public class RockerBuilderGenerator implements PluginGenerator {

  @Value("${jrocks.version}")
  private String version;

  @PostConstruct
  public void postConstruct() {
  }

  @Override
  public String description() {
    return "Builder default generator";
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public List<GeneratedSource> generate(ClassParameterApi parameter, ClassApi classApi) {

    String content = Rocker.template("jrocks/templates/builder.rocker.raw", classApi, parameter).render().toString();
    String packageName = parameter.getUserResponse(RockerBuilderPlugin.Q_PACKAGE)
        .map(UserResponse::text)
        .orElse(classApi.packageName());

    return Collections.singletonList(new GeneratedSourceSupport()
        .setContent(content)
        .setFilename(classApi.simpleName() + parameter.suffix())
        .setPath(classApi.sourceClassPath())
        .setPackageName(packageName));
  }
}
