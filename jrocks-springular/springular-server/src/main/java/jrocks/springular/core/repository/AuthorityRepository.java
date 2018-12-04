package jrocks.springular.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jrocks.springular.core.model.Authority;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
  Authority findByName(String name);
}
