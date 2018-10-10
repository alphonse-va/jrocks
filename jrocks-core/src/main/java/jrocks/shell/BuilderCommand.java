package jrocks.shell;

import jrocks.ClassPathScanner;
import jrocks.api.ClassInfoApi;
import jrocks.model.BeanMetaDataBuilder;
import jrocks.shell.valueproviders.ClassFieldsValueProvider;
import jrocks.template.bean.builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.google.common.reflect.ClassPath.ClassInfo;
import static java.lang.String.format;

@ShellComponent
public class BuilderCommand {

  private static final Logger LOGGER = LoggerFactory.getLogger(BuilderCommand.class);

  @Autowired
  private ClassPathScanner classPathScanner;

  @Autowired
  private ClassValuesProvider classValuesProvider;


  @ShellMethod(value = "Generate a builder class", key = "builder", group = "builder")
  public String builder(
      @ShellOption(value = "--class", help = "Source class from which you want to generate a builder", valueProvider = ClassValuesProvider.class) String classCanonicalName,
      @ShellOption(value = "--force") boolean force,
      @ShellOption(value = "--all", defaultValue = "true") boolean all,
      @ShellOption(value = "--excluded-fields", valueProvider = ClassFieldsValueProvider.class) List<String> excludedFields) {

    Class<?> sourceClass;
    try {
      sourceClass = Class.forName(classCanonicalName);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(format("Class '%s' not found on the class path", classCanonicalName), e);
    }

    final ClassInfoApi bean = new BeanMetaDataBuilder(sourceClass).build();
    String generatedSource = builder.template(bean).render().toString();
    writeGeneratedFile(bean.canonicalName().replaceAll("\\.", "/"), generatedSource, "Builder", force, sourceClass);

    return "Builder generated with success";
  }

  private boolean writeGeneratedFile(String relativePath, String generatedSource, String className, boolean force, Class<?> clazz) {
    final String sourceDirectory = JRocksConfigHolder.getConfig(JRocksConfigHolder.JRocksConfig.TARGET_SOURCE_DIRECTORY)
        .orElseThrow(() -> new IllegalStateException(JRocksConfigHolder.JRocksConfig.TARGET_SOURCE_DIRECTORY.getKey() + " not found!"));


    final String destPath = format("%s/%s/%s%s.java",
        JRocksConfigHolder.PROJECT_BASE_DIRECTORY,
        sourceDirectory,
        relativePath,
        className);

    try {
      final Path path = Paths.get(destPath);
      if (path.toFile().exists() && !force) {
        LOGGER.error("'{}' file exists, please use --overwrite if you want to", path.toString());
        return true;
      }
      final boolean newFile = path.toFile().createNewFile();
      final Path savedFile = Files.write(path, generatedSource.getBytes());
      LOGGER.info("[builder] - '{}' class generated with success.\n\nSource: {}\nGenerated: {}\n", savedFile.getFileName(), clazz + ".java", savedFile);
    } catch (IOException e) {
      throw new IllegalStateException(e.getLocalizedMessage(), e);
    }

    return false;
  }

  @Component
  public class ClassValuesProvider extends ValueProviderSupport {


    @Autowired
    private ClassPathScanner scanner;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
        return scanner.getAllCanonicalNames().stream().map(CompletionProposal::new).collect(Collectors.toList());
    }

    private boolean hasPublicEmptyConstructor(ClassInfo classInfo) {
      return Stream.of(classInfo.load().getConstructors()).anyMatch(constr -> constr.getParameterCount() == 0);
    }
  }
}
