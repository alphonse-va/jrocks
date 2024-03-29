package jrocks.plugin.javapoet;

import com.squareup.javapoet.*;
import jrocks.plugin.api.*;
import jrocks.plugin.javapoet.util.PoeticUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@Qualifier(JavaPoetBuilderPlugin.LAYOUT_QUALIFIER)
public class JavaPoetBuilderGenerator implements PluginGenerator {

  @Value("${jrocks.version}")
  private String version;

  @Override
  public String description() {
    return "JavaPoet Builder Generator Example";
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public List<GeneratedSource> generate(ClassParameterApi parameter, ClassApi classApi) {
    ClassName sourceClassName = ClassName.bestGuess(classApi.name());

    String packageName = parameter.getUserResponse(JavaPoetBuilderPlugin.Q_PACKAGE)
        .map(UserResponse::text)
        .orElse(classApi.packageName());

    ClassName builderClassName = ClassName.bestGuess(packageName + "." + classApi.simpleName() + parameter.suffix());
    TypeSpec.Builder builderTypeBuilder = TypeSpec.classBuilder(builderClassName)
        .addModifiers(Modifier.PUBLIC)
        .addField(FieldSpec
            .builder(sourceClassName, classApi.propertyName(), Modifier.PRIVATE)
            .initializer("new $T()", sourceClassName).build());

    MethodSpec.Builder builderMethod = MethodSpec.methodBuilder("build").addModifiers(Modifier.PUBLIC);
    classApi.fields().forEach(field -> {
      builderTypeBuilder.addMethod(PoeticUtils.buildWithMethod(classApi.propertyName(), field, builderClassName));
      if (field.isRequired() || parameter.mandatoryFields().contains(field.fieldName())) {
        if (field.getter().isPresent())
          builderMethod.addStatement("$T.requireNonNull($L.$L())", Objects.class, classApi.propertyName(), field.getter().get()).build();
      }
    });
    builderMethod.addStatement("return $L", classApi.propertyName()).returns(sourceClassName);

    TypeSpec builderType = builderTypeBuilder.addMethod(builderMethod.build()).build();
    String content = JavaFile.builder(packageName, builderType).build().toString();
    return Collections.singletonList(new GeneratedSourceSupport()
        .setContent(content)
        .setFilename(classApi.simpleName() + parameter.suffix())
        .setFilename(classApi.simpleName() + parameter.suffix())
        .setPath(classApi.sourceClassPath())
        .setPackageName(packageName));
  }
}
