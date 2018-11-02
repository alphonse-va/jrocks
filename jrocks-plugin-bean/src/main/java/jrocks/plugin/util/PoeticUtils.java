package jrocks.plugin.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import jrocks.plugin.api.FieldApi;

import javax.lang.model.element.Modifier;

public class PoeticUtils {

  public static MethodSpec setterFor(FieldApi field, ClassName returnType) {
    return setterFor("set", field, returnType);
  }

  public static MethodSpec setterFor(String suffix, FieldApi field, ClassName returnType) {
    ClassName fieldType = ClassName.bestGuess(field.name());

    return MethodSpec.methodBuilder(suffix + field.fieldNameCapitalized())
        .addModifiers(Modifier.PUBLIC)
        .addParameter(fieldType, field.fieldName())
        .addStatement("this.$L = $L", field.fieldName(), field.fieldName())
        .addStatement("return $T")
        .returns(returnType)
        .build();
  }

  public static MethodSpec withMethodSpecFor(String fieldName, FieldApi field, ClassName returnType) {
    ClassName fieldType = ClassName.bestGuess(field.name());

    return MethodSpec.methodBuilder("with" + field.fieldNameCapitalized())
        .addModifiers(Modifier.PUBLIC)
        .addParameter(fieldType, field.fieldName())
        .addStatement("$L.set$L($L)", fieldName, field.fieldNameCapitalized(), field.fieldName())
        .addStatement("return this")
        .returns(returnType)
        .build();
  }
}
