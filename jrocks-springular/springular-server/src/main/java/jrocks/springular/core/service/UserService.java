package jrocks.springular.core.service;

import java.util.List;
import jrocks.springular.core.model.User;
import jrocks.springular.core.model.UserRequest;

/**
 * Created by fan.jin on 2016-10-15.
 */
public interface UserService {
  void resetCredentials();

  User findById(Long id);

  User findByUsername(String username);

  List<User> findAll();

  User save(UserRequest user);
}
