package jrocks.shell.valueproviders;

import org.junit.jupiter.api.Test;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class ClassFieldsValueProviderTest {

  private ClassFieldsValueProvider valueProvider = new ClassFieldsValueProvider();
  private static final List<String> VALID_COMMAND_LINE_PARAMETER = Arrays.asList("--class", "jrocks.shell.valueproviders.ClassFieldsValueProviderTest", "--exclude-properties");

  protected String testField;

  @Test
  void complete() {
    final List<CompletionProposal> actual = valueProvider.complete(null, new CompletionContext(VALID_COMMAND_LINE_PARAMETER, 1, 1), null);
    assertThat(actual).extracting(CompletionProposal::value).contains("valueProvider");
  }

  // TODO only accessible fields, public or with mutator
  @Test
  void getCompletionProposals() {
    final List<CompletionProposal> actual = valueProvider.getCompletionProposals(ClassFieldsValueProviderTest.class);
    assertThat(actual).extracting(CompletionProposal::value).contains("valueProvider");
  }

  @Test
  void getSourceClass() {
    final CompletionContext completionContext = new CompletionContext(VALID_COMMAND_LINE_PARAMETER, 1, 1);
    final Optional<Class<?>> actual = valueProvider.getSourceClass(completionContext);
    assertThat(actual).isPresent();
  }
}