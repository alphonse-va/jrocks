package jrocks.plugin.bean;

import com.squareup.javapoet.*;
import jrocks.plugin.api.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;

@Component
@Qualifier(DtoPlugin.LAYOUT_QUALIFIER)
public class DtoDefaultLayout implements PluginLayout {

  @Override
  public String name() {
    return "default";
  }

  @Override
  public List<GeneratedSource> generate(ClassParameterApi parameter, ClassApi classApi) {

    ClassName sourceClassName = ClassName.bestGuess(classApi.name());
    ClassName dtoClassName = ClassName.bestGuess(classApi.name() + parameter.suffix());

    // from method
    MethodSpec.Builder fromMethod = MethodSpec
        .methodBuilder("from")
        .returns(dtoClassName)
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addParameter(sourceClassName, classApi.propertyName())
        .addStatement("$T $L = new $T()", dtoClassName, "dto", dtoClassName);

    // to method
    MethodSpec.Builder toMethod = MethodSpec
        .methodBuilder("to" + classApi.simpleName())
        .returns(sourceClassName)
        .addModifiers(Modifier.PUBLIC)
        .addStatement("$T $L = new $T()", sourceClassName, "result", sourceClassName);

    TypeSpec.Builder dtoTypeBuilder = TypeSpec.classBuilder(dtoClassName)
        .addModifiers(Modifier.PUBLIC);

    classApi.fields().forEach(field -> {

      // fields
      dtoTypeBuilder.addField(FieldSpec.builder(ClassName.bestGuess(field.name()), field.fieldName(), Modifier.PRIVATE).build());

      // from statements
      if(field.getter().isPresent() && field.setter().isPresent()) {
        fromMethod.addStatement("dto.$L($L.$L())", field.setter().get(), classApi.propertyName(), field.getter().get());
      }

      // to statements
      if(field.getter().isPresent() && field.setter().isPresent()) {
        toMethod.addStatement("result.$L(this.$L())", field.setter().get(), field.getter().get());
      }

      // setter
      if (field.setter().isPresent()) {
        dtoTypeBuilder.addMethod(MethodSpec.methodBuilder(field.setter().get())
            .addModifiers(Modifier.PUBLIC)
            .returns(dtoClassName)
            .addParameter(ClassName.bestGuess(field.name()), field.fieldName())
            .addStatement("this.$L = $L", field.fieldName(), field.fieldName())
            .addStatement("return this").build());
      }

      // getter
      if(field.getter().isPresent()) {
        dtoTypeBuilder.addMethod(MethodSpec.methodBuilder(field.getter().get())
            .addModifiers(Modifier.PUBLIC)
            .returns(ClassName.bestGuess(field.name()))
            .addStatement("return $L", field.fieldName()).build());
      }

    });
    fromMethod.addStatement("return dto");
    toMethod.addStatement("return result");
    dtoTypeBuilder
        .addMethod(fromMethod.build())
        .addMethod(toMethod.build());

    String content = JavaFile.builder(classApi.packageName(), dtoTypeBuilder.build()).build().toString();
    return Collections.singletonList(new GeneratedSourceSupport().setContent(content).setPath(classApi.getSourceClassPath()));
  }
}
