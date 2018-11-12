package jrocks.plugin.bean;

import com.squareup.javapoet.*;
import jrocks.plugin.api.*;
import jrocks.plugin.util.PoeticUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@Qualifier(BuilderPlugin.LAYOUT_QUALIFIER)
public class BuilderDefaultGenerator implements PluginGenerator {

  @Value("${jrocks.version}")
  private String version;

  @Override
  public String name() {
    return "default";
  }

  @Override
  public String description() {
    return "Builder default generator";
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public List<GeneratedSource> generate(ClassParameterApi parameter, ClassApi classApi) {
    ClassName sourceClassName = ClassName.bestGuess(classApi.name());

    String packageName = parameter.userResponse(BuilderPlugin.Q_PACKAGE)
        .map(QuestionResponse::text)
        .orElse(classApi.packageName());

    ClassName builderClassName = ClassName.bestGuess(packageName + "." + classApi.simpleName() + parameter.suffix());

    FieldSpec.Builder buildingField = FieldSpec
        .builder(sourceClassName, classApi.propertyName(), Modifier.PRIVATE)
        .initializer("new $T()", sourceClassName);

    TypeSpec.Builder builderTypeBuilder = TypeSpec.classBuilder(builderClassName)
        .addModifiers(Modifier.PUBLIC)
        .addField(buildingField.build());

    MethodSpec.Builder builderMethod = MethodSpec.methodBuilder("build").addModifiers(Modifier.PUBLIC);
    classApi.fields().forEach(field -> {
      builderTypeBuilder.addMethod(PoeticUtils.withMethodSpecFor(classApi.propertyName(), field, builderClassName));
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
        .setPath(classApi.getSourceClassPath())
        .setPackageName(packageName));
  }
}
