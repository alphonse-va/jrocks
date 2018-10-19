package jrocks.shell.generator.dto;

import jrocks.api.ClassInfoApi;
import jrocks.api.ClassInfoParameterApi;
import jrocks.shell.TerminalLogger;
import jrocks.shell.config.JRocksConfig;
import jrocks.shell.config.JRocksProjectConfig;
import jrocks.shell.generator.BaseTemplateGenerator;
import jrocks.shell.parameter.BaseClassInfoParameter;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import jrocks.template.bean.dto;
import jrocks.template.bean.mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DtoTemplateGenerator extends BaseTemplateGenerator {

  private static final String WITH_FACTORY_METHODS_FLAG = "with-factory-method";

  @Autowired
  protected DtoTemplateGenerator(JRocksConfig config, JRocksProjectConfig projectConfig, TerminalLogger logger) {
    super(config, projectConfig, logger);
  }

  @Override
  public void generateSource(ClassInfoParameterApi parameter, ClassInfoApi classInfo) {

    DtoParameter dtoParameter = new DtoParameterBuilder()
        .setClassInfoParameter(parameter)
        .setWithFactoryMethod(parameter.getAdditionalFlags().contains(WITH_FACTORY_METHODS_FLAG))
        .build();

    String generatedSource = dto.template(classInfo, dtoParameter).render().toString();
    writeSource(generatedSource, parameter, classInfo);

    if (!dtoParameter.withFactoryMethod()) {
      BaseClassInfoParameter mapperParameter = new BaseClassInfoParameterBuilder()
          .setClassCanonicalName(parameter.getClassCanonicalName())
          .setForce(parameter.isForce())
          .setSuffix(parameter.suffix() + "Mapper")
          .setSuffixToRemove(parameter.suffixToRemove())
          .build();
      String generatedMapperSource = mapper.template(classInfo, dtoParameter).render().toString();
      writeSource(generatedMapperSource, mapperParameter, classInfo);
    }
  }

  @Override
  public String getName() {
    return "DTO";
  }

  @Override
  public List<String> getAddtionalFlags() {
    return Collections.singletonList(WITH_FACTORY_METHODS_FLAG);
  }

  @Override
  public String getSuffix() {
    return "Dto";
  }
}
