package jrocks.plugin.test.model;

import java.util.List;

public class Customer extends User {

  private String customerId;

  private List<BankAccount> accounts;

  private List<InvestmentAccount> investmentAccounts;

  public String getCustomerId() {
    return customerId;
  }

  public Customer setCustomerId(String customerId) {
    this.customerId = customerId;
    return this;
  }

  public List<BankAccount> getAccounts() {
    return accounts;
  }

  public Customer setAccounts(List<BankAccount> accounts) {
    this.accounts = accounts;
    return this;
  }

  public List<InvestmentAccount> getInvestmentAccounts() {
    return investmentAccounts;
  }

  public Customer setInvestmentAccounts(List<InvestmentAccount> investmentAccounts) {
    this.investmentAccounts = investmentAccounts;
    return this;
  }
}