#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package};

import com.google.common.collect.ImmutableList;
import jrocks.plugin.api.ClassApi;
import jrocks.plugin.api.ClassParameter;
import jrocks.plugin.api.FieldApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExamplePluginTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(ExamplePluginTest.class);

  private ExamplePlugin examplePlugin;

  @BeforeEach
  void setUp() {
    examplePlugin = new ExamplePlugin(Collections.singletonList(new ExampleDefaultLayout()));
  }

  @Test
  void generateSources() {
    ClassParameterApi parameterMock = mock(ClassParameterApi.class);
    when(parameterMock.suffix()).thenReturn("Example");

    FieldApi nameField = mock(FieldApi.class);
    when(nameField.fieldName()).thenReturn("name");
    when(nameField.fieldNameCapitalized()).thenReturn("Name");
    when(nameField.name()).thenReturn("String");
    when(nameField.packageName()).thenReturn("java.lang");
    when(nameField.getter()).thenReturn(Optional.of("getName"));
    when(nameField.setter()).thenReturn(Optional.of("setName"));

    FieldApi dateField = mock(FieldApi.class);
    when(dateField.fieldName()).thenReturn("myDate");
    when(dateField.fieldNameCapitalized()).thenReturn("MyDate");
    when(dateField.name()).thenReturn("java.util.Date");
    when(dateField.packageName()).thenReturn("java.util");
    when(dateField.isRequired()).thenReturn(true);
    when(dateField.getter()).thenReturn(Optional.of("getMyDate"));
    when(dateField.setter()).thenReturn(Optional.of("setMyDate"));

    ClassApi classMock = mock(ClassApi.class);
    when(classMock.packageName()).thenReturn("jrocks.mock");
    when(classMock.name()).thenReturn("jrocks.mock.Mock");
    when(classMock.simpleName()).thenReturn("Mock");
    when(classMock.propertyName()).thenReturn("mock");
    when(classMock.fields()).thenReturn(ImmutableList.of(nameField, dateField));

    final String actualContent = examplePlugin.layouts().get(0).generate(parameterMock, classMock).get(0).content();

    LOGGER.debug(actualContent);

    Assertions.assertThat(actualContent).contains("Example");
  }
}