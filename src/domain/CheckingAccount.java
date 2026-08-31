package domain;

import service.BankException;

public class CheckingAccount extends Account {

    private Double limit;
    private Double balance;

    public CheckingAccount(Integer num, Integer agency, Double balance, Client holder) {
        super(num, agency, balance, holder);
    }

    public Double getBalance() {
        return balance;
    }

    public Double getLimit() {
        return limit;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }

    public void whitDraw(Double value){
        if(value < limit) {
            balance -= value;
        }
         else {
             throw new BankException("The value exceeds the whitdraw limits.");
        }
    }

    public void deposit(Double value){
        balance += value;
    }

}
