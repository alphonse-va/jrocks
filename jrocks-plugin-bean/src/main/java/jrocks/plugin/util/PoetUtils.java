package jrocks.plugin.util;

import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import jrocks.plugin.api.FieldApi;

import javax.lang.model.element.Modifier;

public class PoetUtils {

  public static MethodSpec setterFor(FieldApi field, ClassName returnType) {
    return setterFor("set", field, returnType);
  }

  public static MethodSpec setterFor(String suffix, FieldApi field, ClassName returnType) {
    ClassName fieldType = ClassName.get(field.packageName(), field.name());
    return MethodSpec.methodBuilder(suffix + field.fieldNameCapitalized())
        .addModifiers(Modifier.PUBLIC)
        .addParameter(fieldType, field.fieldName())
        .addStatement("this.$L = $L", field.fieldName(), field.fieldName())
        .addStatement("return $L", returnType)
        .returns(returnType)
        .build();
  }
}
