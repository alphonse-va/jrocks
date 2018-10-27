package jrocks.generator.bean.builder;

import jrocks.generator.test.AbstractTemplateSmokeTest;
import jrocks.model.ClassInfo;
import jrocks.model.ClassInfoBuilder;
import jrocks.samples.model.Matrix;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;

import static org.assertj.core.api.Assertions.assertThat;

class BuilderTemplateSmokeTest extends AbstractTemplateSmokeTest {

  private ClassInfo classInfo;

  @BeforeEach
  void beforeEach() {
    io.github.classgraph.ClassInfo classInfo = classes.get("jrocks.samples.model.Matrix");
    this.classInfo = new ClassInfoBuilder(classInfo).build();
  }

  @Test
  void builder() {
    String actual = builder.template(classInfo, new BaseClassInfoParameterBuilder()
        .setClassCanonicalName(Matrix.class.getCanonicalName())
        .setForce(false)
        .build())
        .render().toString();

    assertThat(actual).contains("MatrixBuilder", "setUsername", "setPassword", "setDecimal");
    assertThatClassCompile(new SimpleEntry<>("jrocks.MatrixBuilder", actual));
  }

}
