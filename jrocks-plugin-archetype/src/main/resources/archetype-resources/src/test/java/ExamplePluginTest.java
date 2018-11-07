#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.google.common.collect.ImmutableList;
import ${package}.api.ClassApi;
import ${package}.api.ClassParameter;
import ${package}.api.FieldApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExamplePluginTest {

  private ExamplePlugin examplePlugin;

  @BeforeEach
  void setUp() {
    examplePlugin = new ExamplePlugin(Collections.singletonList(new ExampleDefaultLayout()));
  }

  @Test
  void generateSources() {
    ClassParameter parameterMock = mock(ClassParameter.class);
    ClassApi classMock = mock(ClassApi.class);
    when(parameterMock.suffix()).thenReturn("Example");

    when(classMock.packageName()).thenReturn("jrocks.mock");
    when(classMock.name()).thenReturn("jrocks.mock.Mock");
    when(classMock.simpleName()).thenReturn("Mock");
    when(classMock.propertyName()).thenReturn("mock");

    FieldApi nameField = mock(FieldApi.class);
    when(nameField.fieldName()).thenReturn("name");
    when(nameField.fieldNameCapitalized()).thenReturn("Name");
    when(nameField.name()).thenReturn("String");
    when(nameField.packageName()).thenReturn("java.lang");
    when(nameField.getter()).thenReturn(Optional.of("getName"));
    when(nameField.setter()).thenReturn(Optional.of("setName"));

    FieldApi date = mock(FieldApi.class);
    when(date.fieldName()).thenReturn("myDate");
    when(date.fieldNameCapitalized()).thenReturn("MyDate");
    when(date.name()).thenReturn("java.util.Date");
    when(date.packageName()).thenReturn("java.util");
    when(date.isRequired()).thenReturn(true);
    when(date.getter()).thenReturn(Optional.of("getMyDate"));
    when(date.setter()).thenReturn(Optional.of("setMyDate"));

    when(classMock.fields()).thenReturn(ImmutableList.of(nameField, date));

    System.out.println(examplePlugin.layouts().get(0).generate(parameterMock, classMock).get(0).content());

  }
}