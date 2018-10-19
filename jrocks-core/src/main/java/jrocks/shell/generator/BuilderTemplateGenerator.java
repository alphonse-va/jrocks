package jrocks.shell.generator;

import jrocks.api.ClassInfoApi;
import jrocks.api.ClassInfoParameterApi;
import jrocks.shell.TerminalLogger;
import jrocks.shell.config.JRocksConfig;
import jrocks.shell.config.JRocksProjectConfig;
import jrocks.template.bean.builder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BuilderTemplateGenerator extends BaseTemplateGenerator {

  @Autowired
  protected BuilderTemplateGenerator(JRocksConfig config, JRocksProjectConfig projectConfig, TerminalLogger logger) {
    super(config, projectConfig, logger);
  }

  @Override
  public void generateSource(ClassInfoParameterApi parameter, ClassInfoApi classInfo) {
    String generatedSource = builder.template(classInfo, parameter).render().toString();
    writeSource(generatedSource, parameter, classInfo);
  }

  @Override
  public String getName() {
    return "BUILDER";
  }

  @Override
  public String getSuffix() {
    return "Builder";
  }
}
