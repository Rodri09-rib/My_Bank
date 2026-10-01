package repository;

import domain.Account;

import java.util.ArrayList;
import java.util.List;

public class RepositoryAccount {

    private final List<Account> listAccount;

    public RepositoryAccount() {
        this.listAccount = new ArrayList<>();
    }

    public boolean save(Account account) {
        if (account == null) {
            throw new IllegalArgumentException("A conta não pode ser nula.");
        }
        if (searchNum(account.getNum()) != null) {
            return false;
        }
        this.listAccount.add(account);
        return true;
    }

    public Account searchNum(int n) {
        for (Account account : this.listAccount) {
            if (account.getNum() == n) {
                return account;
            }
        }
        return null;
    }

    public List<Account> listAll() {
        return List.copyOf(this.listAccount);
    }

    public boolean delete(int num) {
        Account account = searchNum(num);
        return account != null && this.listAccount.remove(account);
    }
}