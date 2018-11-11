package jrocks.plugin.api;

import java.util.StringJoiner;

public class QuestionResponseSupport implements QuestionResponse {

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

  public QuestionResponseSupport setQuestion(Question question) {
    this.question = question;
    return this;
  }

  public QuestionResponseSupport setResponse(String response) {
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
