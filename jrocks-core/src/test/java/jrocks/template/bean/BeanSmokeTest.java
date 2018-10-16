package jrocks.template.bean;

import jrocks.api.ClassInfoApi;
import jrocks.model.BaseClassInfoBuilder;
import jrocks.samples.model.Matrix;
import jrocks.shell.JLineTerminalLogger;
import jrocks.shell.command.bean.DtoParameter;
import jrocks.shell.command.bean.DtoParameterBuilder;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import jrocks.template.AbstractSmokeTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;

import static org.assertj.core.api.Assertions.assertThat;

class BeanSmokeTest extends AbstractSmokeTest {

  private ClassInfoApi infoApi;

  @BeforeEach
  void beforeEach() {
    infoApi = new BaseClassInfoBuilder(Matrix.class).build();
  }

  @Test
  void builder() {
    final String actual = builder.template(infoApi, new BaseClassInfoParameterBuilder()
        .setClassCanonicalName(Matrix.class.getCanonicalName())
        .setForce(false)
        .setExcludedFields(null)
        .setIncludedFields(null)
        .setMandatoryFields(null)
        .build())
        .render().toString();

    assertThat(actual).contains("MatrixBuilder", "setUsername", "setPassword", "setDecimal");
    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixBuilder", actual));
  }

  @Test
  void dto() {
    final DtoParameter dtoParameter = new DtoParameterBuilder()
        .setClassInfoParameter(new BaseClassInfoParameterBuilder()
            .setClassCanonicalName(Matrix.class.getCanonicalName())
            .setSuffix("Dto")
            .build())
        .build();
    final String actual = dto.template(infoApi, dtoParameter).render().toString();
    assertThat(actual).contains("MatrixDto", "setUsername", "setPassword", "setDecimal");
    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixDto", actual));
  }

//  @Test
//  void dtoWithFactoryMethod() {
//    final MapperData mapperData = new MapperData().setSuffix("Dto").setWithFactoryMethod(true);
//    final String actual = dto.template(infoApi, mapperData).render().toString();
//    assertThat(actual).contains("MatrixDto", "setUsername", "setPassword", "setDecimal");
//    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixDto", actual));
//  }
//
//  @Test
//  void dtoWithReplaceSuffix() {
//    final ClassInfoApi toMetaData = new BaseClassInfoBuilder(MatrixTo.class).build();
//    final MapperData dtoData = new MapperData()
//        .setSuffix("Dto")
//        .setSuffixToRemove("To")
//        .setWithFactoryMethod(true);
//
//    final String actualDto = dto.template(toMetaData, dtoData).render().toString();
//
//    assertThat(actualDto).contains("MatrixDto", "setUsername", "setEmail", "setDecimal");
//
//    final MapperData toData = new MapperData()
//        .setSuffix("To")
//        .setWithFactoryMethod(true);
//    final String actualTo = dto.template(infoApi, toData).render().toString();
//    assertThat(actualTo).contains("MatrixTo", "setUsername", "setEmail", "setDecimal");
//
//    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixDto", actualDto),
//        new SimpleEntry<>("jrocks.MatrixTo", actualTo));
//  }

//  @Test
//  void mapper() {
//    final MapperData mapperData = new MapperData().setSuffix("Dto");
//    final String actual = mapper.template(infoApi, mapperData).render().toString();
//
//    assertThat(actual).contains("MatrixDtoMapper", "setUsername", "setPassword", "setDecimal");
//
//    final String actualDto = dto.template(infoApi, mapperData).render().toString();
//
//    assertThatClassCompile(
//        new SimpleEntry<>("jrocks.MatrixDtoMapper", actual),
//        new SimpleEntry<>("jrocks.MatrixDto", actualDto)
//    );
//  }
}
