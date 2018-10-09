package jrocks.template.jhipster;

import jrocks.model.EntityMetaData;
import jrocks.Matrix;
import jrocks.template.AbstractSmokeTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.AbstractMap.SimpleEntry;

import static org.assertj.core.api.Assertions.assertThat;

class JhipsterTemplatesSmokeTest extends AbstractSmokeTest {

  private EntityMetaData<Matrix, Long> metaData;

  @BeforeEach
  void before() {
    metaData = new EntityMetaData<>(Matrix.class, Long.class);
  }

  @Test
  void repository() {
    final String actual = repository.template(metaData).render().toString();
    assertThat(actual).contains("MatrixRepository");
    System.out.println(actual);
    assertThatClassCompile(new SimpleEntry<>(metaData.canonicalName() + "Repository", actual));
  }

  @Test
  void service() {
    final String actual = service.template(metaData).render().toString();
    assertThat(actual).contains("MatrixService");
    assertThatClassCompile(new SimpleEntry<>(metaData.canonicalName() + "Service", actual));
    System.out.println(actual);

  }

  @Test
  void serviceImpl() {
    final String repositoryCode = repository.template(metaData).render().toString();
    final SimpleEntry<String, String> repositoryEntry = new SimpleEntry<>(metaData.canonicalName() + "Repository", repositoryCode);

    final String actual = serviceimpl.template(metaData).render().toString();
    assertThat(actual).contains("MatrixServiceImpl");
   // assertThatClassCompile(repositoryEntry, new SimpleEntry<>(metaData.canonicalName() + "ServiceImpl", actual));
    System.out.println(actual);

  }

  @Test
  void resource() {
    final String actual = resource.template(metaData).render().toString();
    assertThat(actual).contains("MatrixResource");
    System.out.println(actual);
    //assertThatClassCompile(new SimpleEntry<>(metaData.canonicalName() + "Resource", actual));
  }
}
