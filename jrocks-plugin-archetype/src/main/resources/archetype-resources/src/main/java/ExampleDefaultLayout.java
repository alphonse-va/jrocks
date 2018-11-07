#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.squareup.javapoet.*;
import jrocks.plugin.api.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.lang.model.element.Modifier;
import java.util.Collections;
import java.util.List;

@Component
@Qualifier(ExamplePlugin.LAYOUT_QUALIFIER)
public class ExampleDefaultLayout implements PluginLayout {

  @Override
  public String name() {
    return "default";
  }

  @Override
  public List<GeneratedSource> generate(ClassParameter parameter, ClassApi classApi) {

    ClassName exampleClassName = ClassName.bestGuess(classApi.name() + parameter.suffix());

    TypeSpec.Builder exampleTypeBuilder = TypeSpec.classBuilder(exampleClassName).addModifiers(Modifier.PUBLIC);

    classApi.fields().forEach(field -> {

      // fields
      exampleTypeBuilder.addField(FieldSpec.builder(ClassName.bestGuess(field.name()), field.fieldName(), Modifier.PRIVATE).build());

      // setter
      if (field.setter().isPresent()) {
        exampleTypeBuilder.addMethod(MethodSpec.methodBuilder(field.setter().get())
            .addModifiers(Modifier.PUBLIC)
            .returns(exampleClassName)
            .addParameter(ClassName.bestGuess(field.name()), field.fieldName())
            .addStatement("this.${symbol_dollar}L = ${symbol_dollar}L", field.fieldName(), field.fieldName())
            .addStatement("return this").build());
      }

      // getter
      if(field.getter().isPresent()) {
        exampleTypeBuilder.addMethod(MethodSpec.methodBuilder(field.getter().get())
            .addModifiers(Modifier.PUBLIC)
            .returns(ClassName.bestGuess(field.name()))
            .addStatement("return ${symbol_dollar}L", field.fieldName()).build());
      }

    });
    String content = JavaFile.builder(classApi.packageName(), exampleTypeBuilder.build()).build().toString();
    return Collections.singletonList(new GeneratedSourceSupport().setContent(content).setPath(classApi.getSourceClassPath()));
  }
}
