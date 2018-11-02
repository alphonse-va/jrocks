package jrocks.plugin.bean;

import com.squareup.javapoet.*;
import jrocks.plugin.api.*;
import jrocks.plugin.util.PoetUtils;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BuilderPlugin implements ClassPlugin {

  @Override
  public String names() {
    return "builder";
  }

  @Override
  public String description() {
    return "Generate a builder for selected class";
  }

  @Override
  public List<String> keys() {
    return Collections.singletonList("builder");
  }

  @Override
  public List<GeneratedSource> generateSources(ClassParameter parameter, ClassApi clazz) {

    ClassName sourceClassName = ClassName.get(clazz.packageName(), clazz.simpleName());
    ClassName builderClassName = ClassName.get(clazz.packageName(), clazz.simpleName() + parameter.suffix());

    TypeSpec.Builder builderTypeBuilder = TypeSpec.classBuilder(builderClassName)
        .addField(FieldSpec.builder(sourceClassName, clazz.propertyName(), Modifier.PRIVATE).build());

    MethodSpec.Builder builderMethod = MethodSpec.methodBuilder("build").addModifiers(Modifier.PUBLIC);
    for (FieldApi field : clazz.fields()) {
      builderTypeBuilder.addMethod(PoetUtils.setterFor("with", field, builderClassName));
      if (field.isRequired() || parameter.mandatoryFields().contains(field.fieldName())) {
        builderMethod.addStatement("$T.requireNonNull($L.get$L())", Objects.class, clazz.propertyName(), field.fieldNameCapitalized()).build();
      }
    }
    builderMethod.addStatement("return $L", clazz.propertyName()).returns(sourceClassName);
    TypeSpec builderType = builderTypeBuilder.addMethod(builderMethod.build()).build();
    String content = JavaFile.builder(clazz.packageName(), builderType).build().toString();
    return Collections.singletonList(new GeneratedSourceSupport().setContent(content).setPath(clazz.getSourceClassPath()));
  }
}
