package jrocks.template.bean;

import jrocks.Matrix;
import jrocks.MatrixTo;
import jrocks.model.MapperData;
import jrocks.model.BeanMetaData;
import jrocks.model.BeanMetaDataBuilder;
import jrocks.template.AbstractSmokeTest;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;

import static org.assertj.core.api.Assertions.assertThat;

class BeanSmokeTest extends AbstractSmokeTest {

  private BeanMetaData<Matrix> metaData;

  @BeforeEach
  void beforeEach() {
    metaData = new BeanMetaDataBuilder<>(Matrix.class).build();
  }

  @Test
  void builder() {
    final String actual = builder.template(metaData).render().toString();

    assertThat(actual).contains("MatrixBuilder", "setUsername", "setPassword", "setDecimal");
    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixBuilder", actual));
  }

  @Test
  void dto() {
    final MapperData mapperData = new MapperData().setSuffix("Dto");
    final String actual = dto.template(metaData, mapperData).render().toString();
    assertThat(actual).contains("MatrixDto", "setUsername", "setPassword", "setDecimal");

    System.out.println(actual);

    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixDto", actual));
  }

  @Test
  void dtoWithFactoryMethod() {
    final MapperData mapperData = new MapperData().setSuffix("Dto").setWithFactoryMethod(true);
    final String actual = dto.template(metaData, mapperData).render().toString();
    assertThat(actual).contains("MatrixDto", "setUsername", "setPassword", "setDecimal");

    System.out.println(actual);

    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixDto", actual));
  }

  @Test
  @Ignore
  void dtoWithReplaceSuffix() {
    final BeanMetaData<MatrixTo> toMetaData = new BeanMetaDataBuilder<>(MatrixTo.class).build();
    final MapperData dtoData = new MapperData()
        .setSuffix("Dto")
        .setSuffixToRemove("To")
        .setWithFactoryMethod(true);

    final String actualDto = dto.template(toMetaData, dtoData).render().toString();
    assertThat(actualDto).contains("MatrixDto", "setUsername", "setEmail", "setDecimal");

    System.out.println(actualDto);

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
    System.out.println(actual);

    assertThat(actual).contains("MatrixDtoMapper", "setUsername", "setPassword", "setDecimal");

    final String actualDto = dto.template(metaData, mapperData).render().toString();

    assertThatClassCompile(
        new SimpleEntry<>("jrocks.MatrixDtoMapper", actual),
        new SimpleEntry<>("jrocks.MatrixDto", actualDto)
        );
  }
}
