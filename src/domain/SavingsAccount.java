package domain;

public class SavingsAccount extends Account {

    private double yieldRate;
    private double balance;

    public SavingsAccount(Integer num, Integer agency, Double balance, Client holder){
        super(num, agency, balance, holder);
    }

    public double getBalance() {
        return balance;
    }

    public double getYieldRate() {
        return yieldRate;
    }


    public void setYieldRate(double yieldRate) {
        this.yieldRate = yieldRate;
    }

    public void applyEarnings(){
        double balance = getBalance();

        double yieldvalue = balance * this.yieldRate;

        deposit(yieldvalue);

        System.out.printf ("Earnings : %.2f ", yieldvalue);
    }
}
