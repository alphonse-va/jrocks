package jrocks.springular.service;

import java.util.List;
import jrocks.springular.model.Authority;

public interface AuthorityService {
  List<Authority> findById(Long id);

  List<Authority> findByname(String name);

}
