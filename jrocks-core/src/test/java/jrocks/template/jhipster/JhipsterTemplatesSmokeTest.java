package jrocks.template.jhipster;

import jrocks.model.EntityClassInfo;
import jrocks.samples.model.Matrix;
import jrocks.template.AbstractSmokeTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;

import static org.assertj.core.api.Assertions.assertThat;

class JhipsterTemplatesSmokeTest extends AbstractSmokeTest {

  private EntityClassInfo metaData;

  @BeforeEach
  void before() {
    metaData = new EntityClassInfo(Matrix.class, Long.class);
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
