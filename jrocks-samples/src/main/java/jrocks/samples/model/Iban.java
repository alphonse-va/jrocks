package jrocks.samples.model;

class Iban {

  private String number;

  public Iban(final String number) {this.number = number;}

  public String format() {
    return number;
  }
}
