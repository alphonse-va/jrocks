package jrocks.plugin.bean;

import com.fizzed.rocker.Rocker;
import com.fizzed.rocker.runtime.RockerRuntime;
import jrocks.plugin.api.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;
import java.util.List;

@Component
@Qualifier(BuilderPlugin.LAYOUT_QUALIFIER)
public class BuilderDefaultGenerator implements PluginGenerator {

  @Value("${jrocks.version}")
  private String version;

  @PostConstruct
  public void postConstruct() {
    RockerRuntime.getInstance().setReloading(true);
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
    String packageName = parameter.getUserResponse(BuilderPlugin.Q_PACKAGE)
        .map(QuestionResponse::text)
        .orElse(classApi.packageName());

//    String content = builder.template(classApi, parameter).render().toString();

    String content = Rocker.template("templates/builder.rocker.raw", classApi, parameter).render().toString();

    return Collections.singletonList(new GeneratedSourceSupport()
        .setContent(content)
        .setFilename(classApi.simpleName() + parameter.suffix())
        .setFilename(classApi.simpleName() + parameter.suffix())
        .setPath(classApi.sourceClassPath())
        .setPackageName(packageName));
  }
}
