package jrocks.template.util;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.config.ConfigService;
import jrocks.plugin.api.config.ModuleConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Set;

@Component
class JavaUtil {

  private final ConfigService configService;

  @Autowired
  JavaUtil(ConfigService configService) {
    this.configService = configService;
  }

  public String targetPathFromBean(ClassApi bean) {
    String outputDirectory = getJavaOutputDirectory(bean);
    String packageName = bean.packageName();
    return outputDirectory + File.separator + packageNameToPath(packageName);
  }

  private String packageNameToPath(String packageName) {
    return packageName.replace(".", File.separator);
  }

  private String getJavaOutputDirectory(ClassApi clazz) {
    String outputDirectory = clazz.sourceClassPath().getAbsolutePath();
    Set<ModuleConfig> modules = configService.getConfig().getModules();
    return modules.stream()
        .filter(config -> config.getOutputDirectory().equals(outputDirectory))
        .map(ModuleConfig::getSourceDirectory)
        .findAny()
        .orElse(modules.iterator().next().getSourceDirectory());
  }
}
