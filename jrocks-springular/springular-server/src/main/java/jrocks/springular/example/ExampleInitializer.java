package jrocks.springular.example;

import jrocks.springular.example.model.Example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class ExampleInitializer {

  @Autowired
  public ExampleInitializer(ExampleRepository repository) {
    repository.save(Arrays.asList(
        new Example().setFirstname("Tony").setLastname("Vaillant").setUsername("toto"),
        new Example().setFirstname("Jean").setLastname("Du jardin").setUsername("jdj"),
        new Example().setFirstname("Jan").setLastname("Van hoof").setUsername("jdj"),
        new Example().setFirstname("Denis").setLastname("Van Buiten").setUsername("djb"),
        new Example().setFirstname("Jean").setLastname("Michel").setUsername("jmb"),
        new Example().setFirstname("Alain").setLastname("Chemin").setUsername("chemino"),
        new Example().setFirstname("Jean").setLastname("Dupont").setUsername("jan"),
        new Example().setFirstname("Jean Gabin").setLastname("Dupont").setUsername("jan"),
        new Example().setFirstname("Jean Edoire").setLastname("Dupont").setUsername("jan"),
        new Example().setFirstname("Jean Philippe").setLastname("Dupont").setUsername("jan"),
        new Example().setFirstname("Jean Marc").setLastname("Dupont").setUsername("jan"),
        new Example().setFirstname("Foo").setLastname("Bar").setUsername("foo")
    ));
  }
}
