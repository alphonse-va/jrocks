package jrocks.template.jhipster;

import io.github.classgraph.ClassInfo;
import jrocks.model.ReflectiveEntityClassInfo;
import jrocks.model.ReflectiveClassInfo;
import jrocks.template.AbstractSmokeTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;

import static org.assertj.core.api.Assertions.assertThat;

class JhipsterTemplatesSmokeTest extends AbstractSmokeTest {

  private ReflectiveEntityClassInfo metaData;

  @BeforeEach
  void before() {
    ClassInfo matrixClassInfo = classes.get("jrocks.samples.model.Matrix");
    ReflectiveClassInfo longClassInfo = new ReflectiveClassInfo(Long.class);

    metaData = new ReflectiveEntityClassInfo(matrixClassInfo, longClassInfo);
  }

  @Test
  void repository() {
    String actual = repository.template(metaData).render().toString();
    assertThat(actual).contains("MatrixRepository");
    assertThatClassCompile(new SimpleEntry<>(metaData.canonicalName() + "Repository", actual));
  }

  @Test
  void service() {
    String actual = service.template(metaData).render().toString();
    assertThat(actual).contains("MatrixService");
    assertThatClassCompile(new SimpleEntry<>(metaData.canonicalName() + "Service", actual));
  }

  @Test
  void serviceImpl() {
    String repositoryCode = repository.template(metaData).render().toString();
    SimpleEntry<String, String> repositoryEntry = new SimpleEntry<>(metaData.canonicalName() + "Repository", repositoryCode);

    String actual = serviceimpl.template(metaData).render().toString();
    assertThat(actual).contains("MatrixServiceImpl");
    // assertThatClassCompile(repositoryEntry, new SimpleEntry<>(metaData.canonicalName() + "ServiceImpl", actual));
  }

  @Test
  void resource() {
    String actual = resource.template(metaData).render().toString();
    assertThat(actual).contains("MatrixResource");
    //assertThatClassCompile(new SimpleEntry<>(metaData.canonicalName() + "Resource", actual));
  }
}
