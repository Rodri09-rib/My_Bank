package repository;

import domain.Account;

import java.util.ArrayList;
import java.util.List;

public class RepositoryAccount {

    private List<Account> listAccount;

    public RepositoryAccount(){
        this.listAccount = new ArrayList<>();
    }

    public void save(Account c){
        this.listAccount.add(c);
    }
    public Account searchNum(int n){
        for(Account c : this.listAccount){
            if(c.getNum() == n){
                return  c;
            }
        }
        return  null;
    }

    public List<Account> listAll(){
        return this.listAccount;
    }

    public boolean delete(int num){
        Account account = searchNum(num);
        if(account != null){
            return this.listAccount.remove(account);
        }
        return false;
    }
}
