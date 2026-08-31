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

    public void deposit(Double value){

    }

    public void whitDraw(Double value){

    }

}
