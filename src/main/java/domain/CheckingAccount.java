package domain;

import service.BankException;

public class CheckingAccount extends Account {

    private Double limit = null;

    public CheckingAccount(int num, int agency, double balance, Client holder) {
        super(num, agency, balance, holder);
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        if (limit < 0) {
            throw new BankException("O limite de saque não pode ser negativo.");
        }
        this.limit = limit;
    }

    @Override
    public void whitDraw(double value) {
        if (this.limit != null && value > this.limit) {
            throw new BankException("O valor do saque excede o limite de " + this.limit + ".");
        }
        super.whitDraw(value);
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Limite " + (this.limit == null ? "sem limite" : this.limit);
    }
}