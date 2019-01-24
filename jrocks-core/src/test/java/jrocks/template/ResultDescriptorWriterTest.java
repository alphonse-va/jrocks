package jrocks.template;

import jrocks.plugin.api.shell.TerminalLogger;
import jrocks.plugin.api.template.ResultDescriptor;
import jrocks.plugin.api.template.TemplateContext;
import jrocks.shell.parameter.BaseClassInfoParameterBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

@ExtendWith(MockitoExtension.class)
class ResultDescriptorWriterTest {

  private ResultDescriptorWriter writer;

  @BeforeEach
  void before(@Mock TerminalLogger logger) {
    writer = new ResultDescriptorWriter(logger);
  }

  @Test
  void write() {
    String classCanonicalName = ResultDescriptorWriterTest.class.getCanonicalName();
    TemplateContext context = new TemplateContext()
        .setParameter(new BaseClassInfoParameterBuilder().withClassCanonicalName(classCanonicalName).build());
    writer.write(new ResultDescriptor()
        .setContext(context)
        .setFile(new File("target/test/write.test"))
        .setContent("test"));
  }
}