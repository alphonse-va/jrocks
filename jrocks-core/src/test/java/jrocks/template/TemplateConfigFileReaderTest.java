package jrocks.template;

import jrocks.plugin.api.template.TemplateDescriptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TemplateConfigFileReaderTest {

  private TemplateConfigFileReader reader;

  @BeforeEach
  void before() {
    reader = new TemplateConfigFileReader();
    reader.postConstruct();
  }

  @Test
  void readAllDescriptors() {
    List<TemplateDescriptor> descriptors = reader.readAllDescriptors(new File("src/test/resources/jrocks-plugin.yml"));
    assertThat(descriptors).size().isEqualTo(2);
  }
}