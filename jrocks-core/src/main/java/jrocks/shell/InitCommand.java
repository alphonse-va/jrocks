//package jrocks.shell;
//
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Component;
//import picocli.CommandLine;
//import picocli.CommandLine.Command;
//
//import java.io.File;
//
//@Command(name = "init", mixinStandardHelpOptions = true, description = "Initialize JRocks")
//@Component
//public class InitCommand extends AbstractCommand {
//
//  private static final Logger LOGGER = LoggerFactory.getLogger(InitCommand.class);
//
//  @CommandLine.Option(names = {"-p", "--base-package"}, description = "Base package", required = true)
//  private String basePackage;
//
//  @CommandLine.Option(names = {"-f", "--force"}, description = "Regenerate all files")
//  private boolean force;
//
//  @Override
//  protected void execute() {
//
//    if (isMavenProject()) {
//      LOGGER.info("Maven project detected, something useful to do here!!!");
//    }
//
//    if (!JRocksConfigHolder.exists() || force) {
//      JRocksConfigHolder.setConfig(JRocksConfigHolder.JRocksConfig.PROJECT_BASE_PACKAGE, basePackage);
//
//      // TODO: gradle, custom maven conf etc..
//      JRocksConfigHolder.setConfig(JRocksConfigHolder.JRocksConfig.TARGET_SOURCE_DIRECTORY, "src/main/java");
//      JRocksConfigHolder.setConfig(JRocksConfigHolder.JRocksConfig.TARGET_CLASS_DIRECTORY, "target/classes");
//    }
//  }
//
//  private boolean isMavenProject() {
//    return new File(JRocksConfigHolder.PROJECT_BASE_DIRECTORY + "/pom.xml").exists();
//  }
//
//  @Override
//  protected Logger getLogger() {
//    return LOGGER;
//  }
//}
//
