package jrocks.plugin.springular.rocker;

import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.ClassParameterApi;
import jrocks.plugin.api.FieldApi;
import jrocks.plugin.springular.SpringularGenerator;
import jrocks.plugin.springular.SpringularPlugin;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SpringularPluginTest {

  private SpringularPlugin springularPlugin;

  @BeforeEach
  void setUp() {
    springularPlugin = new SpringularPlugin(Collections.singletonList(new SpringularGenerator()));
  }

  @Test
  void generateSources() {
    ClassParameterApi parameter = mock(ClassParameterApi.class);
    ClassApi classMock = mock(ClassApi.class);
    when(parameter.suffix()).thenReturn("Builder");

    when(classMock.packageName()).thenReturn("jrocks.mock");
    when(classMock.name()).thenReturn("jrocks.mock.Mock");
    when(classMock.simpleName()).thenReturn("Mock");
    when(classMock.propertyName()).thenReturn("mock");

    FieldApi nameField = mock(FieldApi.class);
    when(nameField.fieldName()).thenReturn("name");
    when(nameField.fieldNameCapitalized()).thenReturn("Name");
    when(nameField.name()).thenReturn("String");
    when(nameField.packageName()).thenReturn("java.lang");
    when(nameField.isRequired()).thenReturn(true);

    when(classMock.fields()).thenReturn(Collections.singletonList(nameField));

    System.out.println(springularPlugin.generators().get(0).generate(parameter,  classMock).get(0).content());

  }
}