package jrocks.shell.value;

import jrocks.shell.JRocksConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ClassFieldsValueProviderTest {

  private ClassPathScanner scanner = new ClassPathScanner(new JRocksConfig().setBasePackage("jrocks"));
  private ClassFieldsValueProvider valueProvider = new ClassFieldsValueProvider(scanner);

  private static final List<String> VALID_COMMAND_LINE_PARAMETER = new ArrayList<>(Arrays.asList("--class", "jrocks.shell.value.ClassFieldsValueProviderTest", "--exclude-properties"));
  private static final CompletionContext VALID_COMPLETION_CONTEXT = new CompletionContext(VALID_COMMAND_LINE_PARAMETER, 1, 1);

  private String testField;

  public ClassFieldsValueProviderTest setTestField(final String testField) {
    this.testField = testField;
    return this;
  }

  public ClassFieldsValueProviderTest setValueProvider(final ClassFieldsValueProvider valueProvider) {
    this.valueProvider = valueProvider;
    return this;
  }

  @BeforeEach
  void beforeEach() {
    scanner.rebuid();
  }

  @Test
  void complete() {
    final List<CompletionProposal> actual = valueProvider.complete(null, VALID_COMPLETION_CONTEXT, null);
    assertThat(actual).extracting(CompletionProposal::value).contains("valueProvider");
  }

  @Test
  void getCompletionProposals() {
    final List<CompletionProposal> actual = valueProvider.getCompletionProposals(ClassFieldsValueProviderTest.class, VALID_COMPLETION_CONTEXT);
    assertThat(actual).extracting(CompletionProposal::value).contains("valueProvider");
  }

  @Test
  void getCompletionProposalsShouldAppendPreviewFields() {
    final List<String> commandLineParams = VALID_COMMAND_LINE_PARAMETER;
    final String firstExcludedField = " valueProvider,";
    commandLineParams.add(firstExcludedField);

    final CompletionContext completionContext = new CompletionContext(commandLineParams, 3, firstExcludedField.length());
    final List<CompletionProposal> actual = valueProvider.getCompletionProposals(ClassFieldsValueProviderTest.class, completionContext);
    assertThat(actual).extracting(CompletionProposal::value).contains("valueProvider,testField");
  }

  @Test
  void getSourceClass() {
    final Optional<Class<?>> actual = valueProvider.getSourceClass(VALID_COMPLETION_CONTEXT);
    assertThat(actual).isPresent();
  }
}