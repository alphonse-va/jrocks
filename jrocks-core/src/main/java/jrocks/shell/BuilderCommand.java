package jrocks.shell;

import jrocks.api.ClassInfoApi;
import jrocks.model.BeanMetaDataBuilder;
import jrocks.template.bean.builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import static com.google.common.reflect.ClassPath.from;
import static java.lang.String.format;

@Component
@ShellComponent
public class BuilderCommand {

  private static final Logger LOGGER = LoggerFactory.getLogger(BuilderCommand.class);

  @Autowired
  private ClassValuesProvider classValuesProvider;


  @ShellMethod(value = "Generate a builder class", key = "builder")
  public String builder(
      @ShellOption(help = "class", valueProvider = ClassValuesProvider.class) String sourceClass,
      @ShellOption(help = "force") boolean force,
      @ShellOption(help = "all", defaultValue = "true") boolean all,
      @ShellOption(help = "excluded-fields") List<String> excludedFields) {

    final Class<?> aClass;
    try {
      aClass = Class.forName(sourceClass);
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException(format("Class '%s' not found on the class path", sourceClass), e);
    }

    final ClassInfoApi bean = new BeanMetaDataBuilder<>(aClass).build();
    String generatedSource = builder.template(bean).render().toString();
    writeGeneratedFile(bean.canonicalName().replaceAll("\\.", "/"), generatedSource, "Builder", force, aClass);

    return "Builder generated with success";
  }

  boolean writeGeneratedFile(String relativePath, String generatedSource, String className, boolean force, Class<?> clazz) {
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
      path.toFile().createNewFile();
      final Path savedFile = Files.write(path, generatedSource.getBytes());
      LOGGER.info("[builder] - '{}' class generated with success.\n\nSource: {}\nGenerated: {}\n", savedFile.getFileName(), clazz + ".java", savedFile);
    } catch (IOException e) {
      throw new IllegalStateException(e.getLocalizedMessage(), e);
    }

    return false;
  }

  @Component
  public class ClassValuesProvider extends ValueProviderSupport {

    @Value("${project.base-package}")
    private String completionPackage;

    List<CompletionProposal> values;

    @Override
    public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
      try {
        return from(BuilderCommand.class.getClassLoader())
            .getTopLevelClassesRecursive(completionPackage)
            .stream()
            .filter(this::hasPublicEmptyConstructor)
            .map(ClassInfo::getName)
            .map(CompletionProposal::new).collect(Collectors.toList());
      } catch (IOException e) {
        throw new IllegalStateException("Error while loading completion for %s", e);
      }
    }

    private boolean hasPublicEmptyConstructor(ClassInfo classInfo) {
      return Stream.of(classInfo.load().getConstructors()).anyMatch(constr -> constr.getParameterCount() == 0);
    }

    public void setCompletionPackage(String completionPackage) {
      this.completionPackage = completionPackage;
    }
  }
}
