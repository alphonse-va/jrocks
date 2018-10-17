package jrocks.samples.model;

public class User {

  private String username;
  private String firstname;
  private String lastname;

  public String getUsername() {
    return username;
  }

  public User setUsername(final String username) {
    this.username = username;
    return this;
  }

  public String getFirstname() {
    return firstname;
  }

  public User setFirstname(final String firstname) {
    this.firstname = firstname;
    return this;
  }

  public String getLastname() {
    return lastname;
  }

  public User setLastname(final String lastname) {
    this.lastname = lastname;
    return this;
  }
}
