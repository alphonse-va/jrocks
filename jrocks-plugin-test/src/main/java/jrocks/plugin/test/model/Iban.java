package jrocks.plugin.test.model;

class Iban {

  private String number;

  public Iban(String number) {this.number = number;}

  public String format() {
    return number;
  }
}
