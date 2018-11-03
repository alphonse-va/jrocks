package jrocks.plugin.test.model;

import javax.validation.constraints.NotNull;

public class User {

  @NotNull
  private String username;

  @NotNull
  private String firstname;

  @NotNull
  private String lastname;

  public String getUsername() {
    return username;
  }

  public User setUsername(String username) {
    this.username = username;
    return this;
  }

  public String getFirstname() {
    return firstname;
  }

  public User setFirstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

  public String getLastname() {
    return lastname;
  }

  public User setLastname(String lastname) {
    this.lastname = lastname;
    return this;
  }
}
