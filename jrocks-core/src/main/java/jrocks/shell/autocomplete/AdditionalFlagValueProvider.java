package jrocks.shell.autocomplete;

import jrocks.shell.command.GeneratorCommandHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.shell.CompletionContext;
import org.springframework.shell.CompletionProposal;
import org.springframework.shell.standard.ValueProviderSupport;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AdditionalFlagValueProvider extends ValueProviderSupport {

  private final GeneratorCommandHolder generatorCommandHolder;

  @Autowired
  public AdditionalFlagValueProvider(GeneratorCommandHolder generatorCommandHolder) {this.generatorCommandHolder = generatorCommandHolder;}

  @Override
  public List<CompletionProposal> complete(MethodParameter parameter, CompletionContext completionContext, String[] hints) {
    return generatorCommandHolder.getCurrentCommand().additionalFlags().stream().map(CompletionProposal::new).collect(Collectors.toList());
  }
}

