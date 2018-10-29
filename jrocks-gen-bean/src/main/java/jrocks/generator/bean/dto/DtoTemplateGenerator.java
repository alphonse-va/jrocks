package jrocks.generator.bean.dto;

import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoParameter;
import jrocks.shell.generator.BaseTemplateGenerator;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

import static jrocks.generator.bean.dto.DtoCommand.WITH_FACTORY_METHODS_FLAG;

@Component
public class DtoTemplateGenerator extends BaseTemplateGenerator {

  /**
   *
   * FIXME: define a generateSource contract that receive a class and returns a list of files to write
   *
   * @param parameter
   * @param classInfo
   */

  @Override
  public void generateSource(ClassInfoParameter parameter, ClassInfo classInfo) {

    DtoParameter dtoParameter = new DtoParameterBuilder()
        .classInfoParameter(parameter)
        .withFactoryMethod(parameter.getAdditionalFlags().contains(WITH_FACTORY_METHODS_FLAG))
        .build();

    String generatedSource = dto.template(classInfo, dtoParameter).render().toString();
    writeSource(generatedSource, parameter, classInfo);

    if (!dtoParameter.withFactoryMethod()) {
      ClassInfoParameter mapperParameter = new BaseClassInfoParameterBuilder()
          .withClassCanonicalName(parameter.classCanonicalName())
          .withForce(parameter.isForce())
          .withSuffix(parameter.suffix() + "Mapper")
          .withSuffixToRemove(parameter.suffixToRemove())
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
