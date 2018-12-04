package jrocks.springular.core.service;

import java.util.List;
import jrocks.springular.core.model.Authority;

public interface AuthorityService {
  List<Authority> findById(Long id);

  List<Authority> findByname(String name);

}
