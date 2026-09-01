package domain;

public abstract class Account {

    private Integer num;
    private Integer agency;
    private Double balance;
    private Client holder;

    public Account(Integer num, Integer agency, Double balance, Client holder){
        this.num = num;
        this.agency = agency;
        this.balance = balance;
        this.holder = holder;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getAgency() {
        return agency;
    }

    public void setAgency(Integer agency) {
        this.agency = agency;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Client getHolder() {
        return holder;
    }

    public void setHolder(Client holder) {
        this.holder = holder;
    }

    public void deposit(Double value){

    }

    public void whitDraw(Double value){

    }

}
