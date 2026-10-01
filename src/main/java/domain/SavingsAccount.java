package domain;

import service.BankException;

public class SavingsAccount extends Account {

    private double yieldRate;

    public SavingsAccount(int num, int agency, double balance, Client holder) {
        super(num, agency, balance, holder);
    }

    public double getYieldRate() {
        return yieldRate;
    }

    public void setYieldRate(double yieldRate) {
        if (yieldRate < 0) {
            throw new BankException("A taxa de rendimento não pode ser negativa.");
        }
        this.yieldRate = yieldRate;
    }

    public double applyEarnings() {
        double earnings = this.getBalance() * this.yieldRate;
        if (earnings > 0) {
            this.deposit(earnings);
        }
        return earnings;
    }

    @Override
    public String toString() {
        return super.toString()
                + " | Rendimento " + yieldRate;
    }
}