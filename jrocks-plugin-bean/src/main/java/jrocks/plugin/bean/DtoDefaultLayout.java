package jrocks.plugin.bean;

import com.squareup.javapoet.*;
import jrocks.plugin.api.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;

@Component
@Qualifier("DtoLayout")
public class DtoDefaultLayout implements PluginLayout {

  @Override
  public String name() {
    return "default";
  }

  @Override
  public List<GeneratedSource> generate(ClassParameter parameter, ClassApi classApi) {

    ClassName sourceClassName = ClassName.bestGuess(classApi.name());
    ClassName dtoClassName = ClassName.bestGuess(classApi.name() + parameter.suffix());

    // factory method
    MethodSpec.Builder fromMethod = MethodSpec
        .methodBuilder("from")
        .returns(dtoClassName)
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addParameter(sourceClassName, classApi.propertyName())
        .addStatement("$T $L = new $T()", dtoClassName, "dto", dtoClassName);

    for (FieldApi field : classApi.fields()) {
      fromMethod.addStatement("dto.$L($L.$L())", field.setter(), classApi.propertyName(), field.getter());
    }
    fromMethod.addStatement("return dto");

    TypeSpec.Builder dtoTypeBuilder = TypeSpec.classBuilder(dtoClassName)
        .addModifiers(Modifier.PUBLIC)
        .addMethod(fromMethod.build());

    classApi.fields().forEach(field -> {
      dtoTypeBuilder.addField(FieldSpec.builder(ClassName.bestGuess(field.name()), field.fieldName(), Modifier.PRIVATE).build());

      // setter
      dtoTypeBuilder.addMethod(MethodSpec.methodBuilder(field.setter())
          .addModifiers(Modifier.PUBLIC)
          .returns(dtoClassName)
          .addParameter(ClassName.bestGuess(field.name()), field.fieldName())
          .addStatement("this.$L = $L", field.fieldName(), field.fieldName())
          .addStatement("return this").build());

      // getter
      dtoTypeBuilder.addMethod(MethodSpec.methodBuilder(field.getter())
          .addModifiers(Modifier.PUBLIC)
          .returns(ClassName.bestGuess(field.name()))
          .addStatement("return $L", field.fieldName()).build());
    });
    String content = JavaFile.builder(classApi.packageName(), dtoTypeBuilder.build()).build().toString();
    return Collections.singletonList(new GeneratedSourceSupport().setContent(content).setPath(classApi.getSourceClassPath()));
  }
}
