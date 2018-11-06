//package jrocks.shell.command;
//
//import jrocks.model.CommandInfo;
//import jrocks.model.PluginInfo;
//import jrocks.shell.ClassPathScanner;
//import jrocks.shell.TerminalLogger;
//import jrocks.shell.config.ConfigServiceImpl;
//import jrocks.shell.config.GlobalConfig;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junitpioneer.jupiter.TempDirectory;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//import java.io.File;
//import java.nio.file.Path;
//import java.util.Collections;
//
//import static org.junitpioneer.jupiter.TempDirectory.*;
//import static org.mockito.ArgumentMatchers.contains;
//import static org.mockito.ArgumentMatchers.endsWith;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//class ConfigurationCommandTest {
//
//  @Mock
//  private GlobalConfig globalConfig;
//
//  @Mock
//  private ClassPathScanner classPathScanner;
//
//  @InjectMocks
//  private ConfigurationCommand configCommand;
//
//  @BeforeEach
//  void beforeEach(@Mock TerminalLogger terminalLogger) {
//    configCommand.setLogger(terminalLogger);
//    configCommand.setClassPathScanner(classPathScanner);
//    configCommand.setConfigService(new ConfigServiceImpl(globalConfig, terminalLogger));
//  }
//
//  @Test
//  void listShouldLogPluginDirUndefined() {
//    configCommand.showPlugins(false);
//    verify(configCommand.terminalLogger()).error(contains("not defined"));
//  }
//
//  @Test
//  void listShouldLogPluginDirNotFound() {
//    when(globalConfig.getPluginDirectory()).thenReturn(new File("./aa/zzzPluginDir"));
//    configCommand.showPlugins();
//    verify(configCommand.terminalLogger()).error(contains("not found"), endsWith("./aa/zzzPluginDir"));
//  }
//
//  @Test
//  @ExtendWith(TempDirectory.class)
//  void listShouldDoNotingWithEmptyDir(@TempDir Path tempDir) {
//    when(globalConfig.getPluginDirectory()).thenReturn(tempDir.toFile());
//    when(classPathScanner.listInstalledPlugins()).thenReturn(Collections.emptyList());
//
//    configCommand.showPlugins(false);
//    verifyZeroInteractions(configCommand.terminalLogger());
//  }
//
//  @Test
//  @ExtendWith(TempDirectory.class)
//  void listShouldLogFiles(@TempDir Path tempDir) {
//    when(globalConfig.getPluginDirectory()).thenReturn(tempDir.toFile());
//    when(classPathScanner.listInstalledPlugins())
//        .thenReturn(Collections.singletonList(
//            new PluginInfo()
//                .setJarFile(new File("test.jar"))
//                .addCommand(new CommandInfo("t", "test", "test"))));
//
//    configCommand.showPlugins(false);
//    verify(configCommand.terminalLogger(), atLeast(1)).info(anyString(), anyString(), anyString(), anyString());
//  }
//}
//
