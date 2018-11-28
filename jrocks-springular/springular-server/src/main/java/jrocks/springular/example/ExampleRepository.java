package jrocks.springular.example;

import jrocks.springular.example.model.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

@RestResource(path = "example")
public interface ExampleRepository extends JpaRepository<Example, Long> {

  @RestResource(path = "filter")

  @Query("SELECT e from Example e " +
      "WHERE UPPER(e.username) like UPPER(:username) " +
      "OR UPPER(e.firstname) like UPPER(:firstname) " +
      "OR UPPER(e.lastname) like UPPER(:lastname)")
  Page<Example> filter(
      @Param("username") String username,
      @Param("lastname") String lastname,
      @Param("firstname") String firstname,
      @Param("pageable") Pageable pageable);
}
