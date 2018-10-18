package jrocks.samples.model;

class BankAccount {

  private Iban accountNumber;

  public Iban getAccountNumber() {
    return accountNumber;
  }

  public BankAccount setAccountNumber(Iban accountNumber) {
    this.accountNumber = accountNumber;
    return this;
  }
}
