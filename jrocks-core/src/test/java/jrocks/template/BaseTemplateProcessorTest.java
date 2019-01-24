package jrocks.template;

import jrocks.model.ReflectiveClassInfo;
import jrocks.plugin.api.shell.TerminalLogger;
import jrocks.plugin.api.template.ResultDescriptor;
import jrocks.plugin.api.template.TemplateContext;
import jrocks.shell.parameter.BaseClassInfoParameterApi;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import jrocks.template.util.TemplateUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class BaseTemplateProcessorTest {

  private BaseTemplateProcessor processor;

  private TemplateContext context;

  @BeforeEach
  void before(@Mock TerminalLogger logger, @Mock TemplateUtil templateUtil) {
    TemplateConfigFileReader reader = new TemplateConfigFileReader();
    reader.postConstruct();
    processor = new BaseTemplateProcessor(reader, logger, new ResultDescriptorWriter(logger), templateUtil);

    ReflectiveClassInfo bean = new ReflectiveClassInfo(BaseTemplateProcessorTest.class);
    BaseClassInfoParameterApi parameter = new BaseClassInfoParameterBuilder()
        .withClassCanonicalName(BaseTemplateProcessorTest.class.getCanonicalName())
        .build();

    context = new TemplateContext().setBean(bean).setParameter(parameter);
  }

  @Test
  void process() {
    List<ResultDescriptor> actual = processor.process(context, new File("src/test/resources/jrocks-plugin.yml"));
    assertThat(actual).size().isEqualTo(2);
  }
}