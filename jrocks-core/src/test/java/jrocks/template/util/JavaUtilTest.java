package jrocks.template.util;

import jrocks.model.ReflectiveClassInfo;
import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.config.ConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class JavaUtilTest {

  private JavaUtil utils;


  @BeforeEach
  void before(@Mock ConfigService service) {
    utils = new JavaUtil(service);
  }

  @Test
  void targetPathFromBean() {

    ClassApi classInfo = new ReflectiveClassInfo(JavaUtilTest.class);

    String actual = utils.targetPathFromBean(classInfo);
    System.out.println(actual);
  }
}