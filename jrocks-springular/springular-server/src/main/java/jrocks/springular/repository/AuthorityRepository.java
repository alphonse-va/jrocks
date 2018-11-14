package jrocks.springular.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jrocks.springular.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
  Authority findByName(String name);
}
