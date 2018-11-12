package jrocks.plugin.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import jrocks.plugin.api.FieldApi;

import javax.lang.model.element.Modifier;

public class PoeticUtils {

  public static MethodSpec buildWithMethod(String fieldName, FieldApi field, ClassName returnType) {
    ClassName fieldType = ClassName.bestGuess(field.name());
    return MethodSpec.methodBuilder("with" + field.fieldNameCapitalized())
        .addModifiers(Modifier.PUBLIC)
        .addParameter(fieldType, field.fieldName())
        .addStatement("$L.set$L($L)", fieldName, field.fieldNameCapitalized(), field.fieldName())
        .addStatement("return this")
        .returns(returnType)
        .build();
  }

  public static MethodSpec buildGetterMethod(FieldApi field) {
    return MethodSpec.methodBuilder(field.getter().orElse("get" + field.simpleName()))
        .addModifiers(Modifier.PUBLIC)
        .returns(ClassName.bestGuess(field.name()))
        .addStatement("return $L", field.fieldName()).build();
  }

  public static MethodSpec buildBuilderSetterMethod(ClassName returnType, FieldApi field) {
    return MethodSpec.methodBuilder(field.setter().orElse("set" + field.simpleName()))
        .addModifiers(Modifier.PUBLIC)
        .returns(returnType)
        .addParameter(ClassName.bestGuess(field.name()), field.fieldName())
        .addStatement("this.$L = $L", field.fieldName(), field.fieldName())
        .addStatement("return this").build();
  }

  public static MethodSpec buildSetterMethod(ClassName returnType, FieldApi field) {
    return MethodSpec.methodBuilder(field.setter().orElse("set" + field.simpleName()))
        .addModifiers(Modifier.PUBLIC)
        .returns(returnType)
        .addParameter(ClassName.bestGuess(field.name()), field.fieldName())
        .addStatement("this.$L = $L", field.fieldName(), field.fieldName())
        .build();
  }

}
