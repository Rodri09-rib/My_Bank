package domain;

import service.BankException;

public abstract class Account {

    private final int num;
    private final int agency;
    private double balance;
    private Client holder;

    protected Account(int num, int agency, double balance, Client holder) {
        if (num <= 0) {
            throw new BankException("O número da conta deve ser maior que zero.");
        }
        if (agency <= 0) {
            throw new BankException("O número da agência deve ser maior que zero.");
        }
        if (balance < 0) {
            throw new BankException("O saldo inicial não pode ser negativo.");
        }
        if (holder == null) {
            throw new BankException("A conta precisa de um titular.");
        }
        this.num = num;
        this.agency = agency;
        this.balance = balance;
        this.holder = holder;
    }

    public int getNum() {
        return num;
    }

    public int getAgency() {
        return agency;
    }

    public double getBalance() {
        return balance;
    }

    public Client getHolder() {
        return holder;
    }

    public void setHolder(Client holder) {
        if (holder == null) {
            throw new BankException("A conta precisa de um titular.");
        }
        this.holder = holder;
    }

    public void deposit(double value) {
        if (value <= 0) {
            throw new BankException("O valor do depósito deve ser maior que zero.");
        }
        this.balance += value;
    }

    public void whitDraw(double value) {
        if (value <= 0) {
            throw new BankException("O valor do saque deve ser maior que zero.");
        }
        if (value > this.balance) {
            throw new BankException("Saldo insuficiente. Disponível: " + this.balance + ".");
        }
        this.balance -= value;
    }

    @Override
    public String toString() {
        return "Conta " + num
                + " | Agência " + agency
                + " | Saldo " + balance
                + " | Titular " + holder.getName()
                + " (" + holder.getCpf() + ")";
    }
}