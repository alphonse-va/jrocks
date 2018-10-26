package jrocks.generator.bean;

import jrocks.generator.bean.dto.DtoParameter;
import jrocks.generator.bean.dto.DtoParameterBuilder;
import jrocks.generator.bean.dto.dto;
import jrocks.generator.test.AbstractSmokeTest;
import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoBuilder;
import jrocks.samples.model.Matrix;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;

import static org.assertj.core.api.Assertions.assertThat;

class BeanSmokeTest extends AbstractSmokeTest {

  private ClassInfo infoApi;

  @BeforeEach
  void beforeEach() {
    io.github.classgraph.ClassInfo classInfo = classes.get("jrocks.samples.model.Matrix");
    infoApi = new ClassInfoBuilder(classInfo).build();
  }

//  @Test
//  void builder() {
//    String actual = builder.template(infoApi, new BaseClassInfoParameterBuilder()
//        .setClassCanonicalName(Matrix.class.getCanonicalName())
//        .setForce(false)
//        .build())
//        .render().toString();
//
//    assertThat(actual).contains("MatrixBuilder", "setUsername", "setPassword", "setDecimal");
//    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixBuilder", actual));
//  }

  @Test
  void dto() {
    DtoParameter dtoParameter = new DtoParameterBuilder()
        .setClassInfoParameter(new BaseClassInfoParameterBuilder()
            .setClassCanonicalName(Matrix.class.getCanonicalName())
            .setSuffix("Dto")
            .build())
        .build();
    String actual = dto.template(infoApi, dtoParameter).render().toString();
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
//    final ClassInfo toMetaData = new ClassInfoBuilder(MatrixTo.class).build();
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
