package jrocks.samples.model;

import javax.validation.constraints.Positive;

public class InvestmentAccount extends BankAccount {

  @Positive
  private Double rate;

  public Double getRate() {
    return rate;
  }

  public InvestmentAccount setRate(Double rate) {
    this.rate = rate;
    return this;
  }
}
