package jrocks.plugin.api;

import java.util.StringJoiner;

public class UserResponseSupport implements UserResponse {

  private Question question;
  private String response;

  @Override
  public Question question() {
    return question;
  }

  @Override
  public String text() {
    return response;
  }

  public UserResponseSupport setQuestion(Question question) {
    this.question = question;
    return this;
  }

  public UserResponseSupport setResponse(String response) {
    this.response = response;
    return this;
  }

  @Override
  public String toString() {
    return new StringJoiner(", ", "response: ", "")
        .add("question: _" + question.text() + "_")
        .add("response: _" + response + "_")
        .toString();
  }
}
