package jrocks.plugin.javapoet;

import com.squareup.javapoet.*;
import jrocks.plugin.api.*;
import jrocks.plugin.javapoet.util.PoeticUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.lang.model.element.Modifier;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

@Component
@Qualifier(JavaPoetDtoPlugin.LAYOUT_QUALIFIER)
public class JavaPoetDtoGenerator implements PluginGenerator {

  @Value("${jrocks.version}")
  private String version;

  @Override
  public String description() {
    return "JavaPoet Dto Generator Example";
  }

  @Override
  public String version() {
    return version;
  }

  @Override
  public List<GeneratedSource> generate(ClassParameterApi parameter, ClassApi classApi) {

    ClassName sourceClassName = ClassName.bestGuess(classApi.name());
    ClassName dtoClassName = ClassName.bestGuess(classApi.name() + parameter.suffix());

    // from method
    String propertyName = classApi.propertyName();
    MethodSpec.Builder fromMethod = MethodSpec.methodBuilder("from")
        .returns(dtoClassName)
        .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
        .addParameter(sourceClassName, propertyName)
        .addStatement("$T $L = new $T()", dtoClassName, propertyName + parameter.suffix(), dtoClassName);

    // to method
    MethodSpec.Builder toMethod = MethodSpec.methodBuilder("to")
        .returns(sourceClassName)
        .addModifiers(Modifier.PUBLIC)
        .addStatement("$T $L = new $T()", sourceClassName, propertyName, sourceClassName);

    if (parameter.hasFlag(JavaPoetDtoPlugin.WITH_MAPPER_FLAG)) {
      toMethod
          .addModifiers(Modifier.STATIC)
          .addParameter(dtoClassName, propertyName + parameter.suffix());
    } else {
      toMethod.addParameter(dtoClassName, propertyName);
    }

    TypeSpec.Builder dtoTypeBuilder = TypeSpec.classBuilder(dtoClassName).addModifiers(Modifier.PUBLIC);

    // build methods
    classApi.fields().forEach(field -> {
      dtoTypeBuilder.addField(FieldSpec.builder(ClassName.bestGuess(field.name()), field.fieldName(), Modifier.PRIVATE).build());
      if (field.getter().isPresent() && field.setter().isPresent()) {
        String setter = field.setter().get();
        String getter = field.getter().get();
        fromMethod.addStatement("$L$L.$L($L.$L())", propertyName, parameter.suffix(), setter, propertyName, getter);
        toMethod.addStatement(format("$L.$L(%s.$L())", resolveFromFieldName(parameter, propertyName)), propertyName, setter, getter);
      }
    });
    fromMethod.addStatement("return $L$L", propertyName, parameter.suffix());
    toMethod.addStatement("return $L", propertyName);

    // factory methods
    if (!parameter.hasFlag(JavaPoetDtoPlugin.WITH_MAPPER_FLAG)) {
      dtoTypeBuilder.addMethod(fromMethod.build()).addMethod(toMethod.build());
    }

    // getters and setters
    classApi.fields().forEach(field -> {
      field.setter().ifPresent(setter -> dtoTypeBuilder.addMethod(PoeticUtils.buildBuilderSetterMethod(dtoClassName, field)));
      field.getter().ifPresent(getter -> dtoTypeBuilder.addMethod(PoeticUtils.buildGetterMethod(field)));
    });

    List<GeneratedSource> result = new ArrayList<>();
    result.add(new GeneratedSourceSupport()
        .setFilename(format("%s%s.java", classApi.simpleName(), parameter.suffix()))
        .setContent(JavaFile.builder(classApi.packageName(), dtoTypeBuilder.build()).build().toString())
        .setPackageName(getPackage(parameter, classApi, JavaPoetDtoPlugin.Q_DTO_PACKAGE))
        .setPath(classApi.sourceClassPath()));

    if (parameter.hasFlag(JavaPoetDtoPlugin.WITH_MAPPER_FLAG)) {
      // mapper class
      String mapperPackage = getPackage(parameter, classApi, JavaPoetDtoPlugin.Q_MAPPER_PACKAGE);
      String mapperClass = format("%s.%s%sMapper", mapperPackage, classApi.simpleName(), parameter.suffix());
      ClassName mapperClassName = ClassName.bestGuess(mapperClass);
      TypeSpec mapperType = TypeSpec.classBuilder(mapperClassName)
          .addModifiers(Modifier.PUBLIC)
          .addMethod(fromMethod.build())
          .addMethod(toMethod.build())
          .build();

      String mapperContent = JavaFile.builder(mapperPackage, mapperType).build().toString();
      result.add(new GeneratedSourceSupport()
          .setContent(mapperContent)
          .setFilename(format("%s%sMapper.java", classApi.simpleName(), parameter.suffix()))
          .setPackageName(mapperPackage)
          .setPath(classApi.sourceClassPath()));
    }
    return result;
  }

  private static String getPackage(ClassParameterApi parameter, ClassApi classApi, String aPackage) {
    return parameter.getUserResponse(aPackage)
        .map(UserResponse::text)
        .orElse(classApi.packageName());
  }

  private static String resolveFromFieldName(ClassParameterApi parameter, String propertyName) {
    return parameter.hasFlag(JavaPoetDtoPlugin.WITH_MAPPER_FLAG) ? propertyName + parameter.suffix() : "this";
  }
}
