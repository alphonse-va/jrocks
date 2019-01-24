package jrocks.template.util;

import jrocks.plugin.api.config.ConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;


@ExtendWith(MockitoExtension.class)
class AngularUtilTest {

  private AngularUtil utils;

  private static final String MODULE_TS = "target/app.module.ts";

  @BeforeEach
  void before(@Mock ConfigService configService) {
    utils = new AngularUtil(configService);
    try {
      Files.write(Paths.get(MODULE_TS), "".getBytes());
    } catch (IOException e) {
      throw new IllegalStateException(e);
    }
  }

  @Test
  void importComponent() {

    utils.importType("src/test/resources/test.component.ts", MODULE_TS);

    // TODO assert that component is well imported into app.module.ts

  }
}