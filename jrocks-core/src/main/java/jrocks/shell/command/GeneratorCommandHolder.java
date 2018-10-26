package jrocks.shell.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class GeneratorCommandHolder {

  @Autowired
  private List<GeneratorCommand> generatorCommands;

  private GeneratorCommand currentCommand;

  public void setCurrentCommand(String commandName) {
    currentCommand = generatorCommands.stream().filter(c -> c.name().equals(commandName))
        .findAny()
        .orElse(null);
  }

  public GeneratorCommand getCurrentCommand() {
    return currentCommand;
  }

  public List<String> getGeneratorNames() {
    return generatorCommands.stream().map(GeneratorCommand::name).collect(Collectors.toList());
  }
}
