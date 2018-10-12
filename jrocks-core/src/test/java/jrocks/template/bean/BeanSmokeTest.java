package jrocks.template.bean;

import jrocks.api.ClassInfoApi;
import jrocks.model.BeanClassInfoBuilder;
import jrocks.model.MapperData;
import jrocks.samples.model.Matrix;
import jrocks.samples.model.MatrixTo;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import jrocks.template.AbstractSmokeTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;

import static org.assertj.core.api.Assertions.assertThat;

class BeanSmokeTest extends AbstractSmokeTest {

  private ClassInfoApi metaData;

  @BeforeEach
  void beforeEach() {
    metaData = new BeanClassInfoBuilder<>(Matrix.class).build();
  }

  @Test
  void builder() {
    final String actual = builder.template(metaData, new BaseClassInfoParameterBuilder().setClassCanonicalName(Matrix.class.getCanonicalName()).setLogLevel(null).setForce(false).setExcludedFields(null).setIncludedFields(null).setMandatoryFields(null).build()).render().toString();

    assertThat(actual).contains("MatrixBuilder", "setUsername", "setPassword", "setDecimal");
    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixBuilder", actual));
  }

  @Test
  void dto() {
    final MapperData mapperData = new MapperData().setSuffix("Dto");
    final String actual = dto.template(metaData, mapperData).render().toString();
    assertThat(actual).contains("MatrixDto", "setUsername", "setPassword", "setDecimal");
    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixDto", actual));
  }

  @Test
  void dtoWithFactoryMethod() {
    final MapperData mapperData = new MapperData().setSuffix("Dto").setWithFactoryMethod(true);
    final String actual = dto.template(metaData, mapperData).render().toString();
    assertThat(actual).contains("MatrixDto", "setUsername", "setPassword", "setDecimal");
    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixDto", actual));
  }

  @Test
  void dtoWithReplaceSuffix() {
    final ClassInfoApi toMetaData = new BeanClassInfoBuilder(MatrixTo.class).build();
    final MapperData dtoData = new MapperData()
        .setSuffix("Dto")
        .setSuffixToRemove("To")
        .setWithFactoryMethod(true);

    final String actualDto = dto.template(toMetaData, dtoData).render().toString();

    assertThat(actualDto).contains("MatrixDto", "setUsername", "setEmail", "setDecimal");

    final MapperData toData = new MapperData()
        .setSuffix("To")
        .setWithFactoryMethod(true);
    final String actualTo = dto.template(metaData, toData).render().toString();
    assertThat(actualTo).contains("MatrixTo", "setUsername", "setEmail", "setDecimal");

    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixDto", actualDto),
        new SimpleEntry<>("jrocks.MatrixTo", actualTo));
  }

  @Test
  void mapper() {
    final MapperData mapperData = new MapperData().setSuffix("Dto");
    final String actual = mapper.template(metaData, mapperData).render().toString();

    assertThat(actual).contains("MatrixDtoMapper", "setUsername", "setPassword", "setDecimal");

    final String actualDto = dto.template(metaData, mapperData).render().toString();

    assertThatClassCompile(
        new SimpleEntry<>("jrocks.MatrixDtoMapper", actual),
        new SimpleEntry<>("jrocks.MatrixDto", actualDto)
    );
  }
}
