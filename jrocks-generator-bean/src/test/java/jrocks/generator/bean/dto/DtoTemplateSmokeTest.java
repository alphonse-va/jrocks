package jrocks.generator.bean.dto;

import jrocks.generator.test.AbstractTemplateSmokeTest;
import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoBuilder;
import jrocks.samples.model.Matrix;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;

import static org.assertj.core.api.Assertions.assertThat;

class DtoTemplateSmokeTest extends AbstractTemplateSmokeTest {

  private ClassInfo classInfo;

  @BeforeEach
  void beforeEach() {
    io.github.classgraph.ClassInfo classInfo = classes.get("jrocks.samples.model.Matrix");
    this.classInfo = new ClassInfoBuilder(classInfo).build();
  }

  @Test
  void dto() {
    DtoParameter dtoParameter = new DtoParameterBuilder()
        .classInfoParameter(new BaseClassInfoParameterBuilder()
            .setClassCanonicalName(Matrix.class.getCanonicalName())
            .setSuffix("Dto")
            .build())
        .build();
    String actual = dto.template(classInfo, dtoParameter).render().toString();
    assertThat(actual).contains("MatrixDto", "setUsername", "setPassword", "setDecimal");
    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixDto", actual));
  }

  @Test
  void dtoWithFactoryMethod() {
    DtoParameter dtoParameter = new DtoParameterBuilder()
        .withFactoryMethod(true)
        .classInfoParameter(new BaseClassInfoParameterBuilder()
            .setClassCanonicalName(Matrix.class.getCanonicalName())
            .setSuffix("Dto")
            .build())
        .build();
    String actual = dto.template(classInfo, dtoParameter).render().toString();
    assertThat(actual).contains("MatrixDto", "setUsername", "setPassword", "setDecimal");
    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixDto", actual));
  }

  @Test
  void dtoMapper() {
    DtoParameter dtoParameter = new DtoParameterBuilder()
        .withFactoryMethod(true)
        .classInfoParameter(new BaseClassInfoParameterBuilder()
            .setClassCanonicalName(Matrix.class.getCanonicalName())
            .setSuffix("Dto")
            .build())
        .build();

    String actual = dto.template(classInfo, dtoParameter).render().toString();
    assertThat(actual).contains("MatrixDto", "setUsername", "setPassword", "setDecimal");

    DtoParameter mapperParameter = new DtoParameterBuilder()
        .classInfoParameter(new BaseClassInfoParameterBuilder()
            .setClassCanonicalName(dtoParameter.getClassCanonicalName())
            .setForce(dtoParameter.isForce())
            .setSuffix(dtoParameter.suffix())
            .setSuffixToRemove(dtoParameter.suffixToRemove())
            .build())
        .build();

    String actualMapper = mapper.template(classInfo, mapperParameter).render().toString();
    assertThat(actualMapper).contains("MatrixDtoMapper", "setUsername", "setPassword", "setDecimal");

    assertThatClassCompile(
        new SimpleEntry<>("jrocks.MatrixDtoMapper", actualMapper),
        new SimpleEntry<>("jrocks.MatrixDto", actual)
    );
  }
}
