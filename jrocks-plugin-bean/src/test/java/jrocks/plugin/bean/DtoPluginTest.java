package jrocks.plugin.bean;

import com.google.common.collect.ImmutableList;
import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.ClassParameter;
import jrocks.plugin.api.FieldApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DtoPluginTest {

  private DtoPlugin dtoPlugin;

  @BeforeEach
  void setUp() {
    dtoPlugin = new DtoPlugin();
  }

  @Test
  void generateSources() {
    ClassParameter parameterMock = mock(ClassParameter.class);
    ClassApi classMock = mock(ClassApi.class);
    when(parameterMock.suffix()).thenReturn("Dto");

    when(classMock.packageName()).thenReturn("jrocks.mock");
    when(classMock.name()).thenReturn("jrocks.mock.Mock");
    when(classMock.simpleName()).thenReturn("Mock");
    when(classMock.propertyName()).thenReturn("mock");

    FieldApi nameField = mock(FieldApi.class);
    when(nameField.fieldName()).thenReturn("name");
    when(nameField.fieldNameCapitalized()).thenReturn("Name");
    when(nameField.name()).thenReturn("String");
    when(nameField.packageName()).thenReturn("java.lang");
    when(nameField.getter()).thenReturn("getName");
    when(nameField.setter()).thenReturn("setName");

    FieldApi date = mock(FieldApi.class);
    when(date.fieldName()).thenReturn("myDate");
    when(date.fieldNameCapitalized()).thenReturn("MyDate");
    when(date.name()).thenReturn("java.util.Date");
    when(date.packageName()).thenReturn("java.util");
    when(date.isRequired()).thenReturn(true);
    when(date.getter()).thenReturn("getMyDate");
    when(date.setter()).thenReturn("setMyDate");

    when(classMock.fields()).thenReturn(ImmutableList.of(nameField, date));

    System.out.println(dtoPlugin.generateSources(parameterMock, classMock).get(0).content());

  }
}