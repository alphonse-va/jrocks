package jrocks.shell.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class GeneratorCommandHolder {

  @Autowired(required = false)
  private List<ClassGeneratorCommand> generatorCommands;

  private ClassGeneratorCommand currentCommand;

  public void setCurrentCommand(String commandName) {
    if (generatorCommands == null) return;
    currentCommand = generatorCommands.stream().filter(c -> c.name().equals(commandName))
        .findAny()
        .orElse(null);
  }

  public ClassGeneratorCommand getCurrentCommand() {
    return currentCommand;
  }

  public List<String> getGeneratorNames() {
    return generatorCommands == null
        ? new ArrayList<>()
        : generatorCommands.stream().map(ClassGeneratorCommand::name).collect(Collectors.toList());
  }
}
