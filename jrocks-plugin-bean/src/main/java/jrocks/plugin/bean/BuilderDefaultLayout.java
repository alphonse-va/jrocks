package jrocks.plugin.bean;

import com.squareup.javapoet.*;
import jrocks.plugin.api.*;
import jrocks.plugin.util.PoeticUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
@Qualifier("BuilderLayout")
public class BuilderDefaultLayout implements PluginLayout {

  @Override
  public String name() {
    return "default";
  }

  @Override
  public List<GeneratedSource> generate(ClassParameter parameter, ClassApi classApi) {
    ClassName sourceClassName = ClassName.bestGuess(classApi.name());
    ClassName builderClassName = ClassName.bestGuess(classApi.name() + parameter.suffix());

    FieldSpec.Builder buildingField = FieldSpec
        .builder(sourceClassName, classApi.propertyName(), Modifier.PRIVATE)
        .initializer("new $T()", sourceClassName);

    TypeSpec.Builder builderTypeBuilder = TypeSpec.classBuilder(builderClassName)
        .addModifiers(Modifier.PUBLIC)
        .addField(buildingField.build());

    MethodSpec.Builder builderMethod = MethodSpec.methodBuilder("generate").addModifiers(Modifier.PUBLIC);
    classApi.fields().forEach(field -> {
      builderTypeBuilder.addMethod(PoeticUtils.withMethodSpecFor(classApi.propertyName(), field, builderClassName));
      if (field.isRequired() || parameter.mandatoryFields().contains(field.fieldName())) {
        builderMethod.addStatement("$T.requireNonNull($L.get$L())", Objects.class, classApi.propertyName(), field.fieldNameCapitalized()).build();
      }
    });
    builderMethod.addStatement("return $L", classApi.propertyName()).returns(sourceClassName);

    TypeSpec builderType = builderTypeBuilder.addMethod(builderMethod.build()).build();
    String content = JavaFile.builder(classApi.packageName(), builderType).build().toString();
    return Collections.singletonList(new GeneratedSourceSupport().setContent(content).setPath(classApi.getSourceClassPath()));
  }
}
