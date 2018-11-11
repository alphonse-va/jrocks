package jrocks.plugin.api;

import java.util.List;

public class QuestionSupport implements Question {

  private String question;
  private String buffer;
  private List<String> proposals;
  private Character mask;

  @Override
  public String text() {
    return question;
  }

  @Override
  public String buffer() {
    return buffer;
  }

  @Override
  public List<String> proposals() {
    return proposals;
  }

  @Override
  public Character mask() {
    return mask;
  }

  public QuestionSupport setQuestion(String question) {
    this.question = question;
    return this;
  }

  public QuestionSupport setProposals(List<String> proposals) {
    this.proposals = proposals;
    return this;
  }

  public QuestionSupport setMask(Character mask) {
    this.mask = mask;
    return this;
  }

  public QuestionSupport setBuffer(String buffer) {
    this.buffer = buffer;
    return this;
  }
}
