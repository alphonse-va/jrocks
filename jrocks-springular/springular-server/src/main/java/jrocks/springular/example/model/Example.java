package jrocks.springular.example.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
public class Example implements Serializable {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name = "FIRSTNAME")
  private String firstname;

  @Column(name = "LASTNAME")
  private String lastname;

  @Column(name = "USERNAME")
  private String username;

  public Long getId() {
    return id;
  }

  public Example setId(Long id) {
    this.id = id;
    return this;
  }

  public String getFirstname() {
    return firstname;
  }

  public Example setFirstname(String firstname) {
    this.firstname = firstname;
    return this;
  }

  public String getLastname() {
    return lastname;
  }

  public Example setLastname(String lastname) {
    this.lastname = lastname;
    return this;
  }

  public String getUsername() {
    return username;
  }

  public Example setUsername(String username) {
    this.username = username;
    return this;
  }
}
