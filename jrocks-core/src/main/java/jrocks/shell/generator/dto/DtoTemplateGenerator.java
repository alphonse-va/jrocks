package jrocks.shell.generator.dto;

import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoParameter;
import jrocks.shell.generator.BaseTemplateGenerator;
import jrocks.shell.generator.TemplateWriterService;
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
  public DtoTemplateGenerator(TemplateWriterService writerService) {
    super(writerService);
  }

  @Override
  public void generateSource(ClassInfoParameter parameter, ClassInfo classInfo) {

    DtoParameter dtoParameter = new DtoParameterBuilder()
        .setClassInfoParameter(parameter)
        .setWithFactoryMethod(parameter.getAdditionalFlags().contains(WITH_FACTORY_METHODS_FLAG))
        .build();

    String generatedSource = dto.template(classInfo, dtoParameter).render().toString();
    writeSource(generatedSource, parameter, classInfo);

    if (!dtoParameter.withFactoryMethod()) {
      ClassInfoParameter mapperParameter = new BaseClassInfoParameterBuilder()
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
  public List<String> additionalFlags() {
    return Collections.singletonList(WITH_FACTORY_METHODS_FLAG);
  }

  @Override
  public String suffix() {
    return "Dto";
  }
}
