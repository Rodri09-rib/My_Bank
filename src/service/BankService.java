package service;

import domain.Account;
import domain.CheckingAccount;
import domain.Client;
import domain.SavingsAccount;
import repository.RepositoryAccount;

import java.util.Map;

public class BankService {

      private final RepositoryAccount repository;

      public BankService() {
            this.repository = new RepositoryAccount();
      }

      public BankService(RepositoryAccount repository) {
            this.repository = repository;
      }

      public void createAccount(Map<String, Object> dados) {
            String name = (String) dados.get("name");
            String cpf = (String) dados.get("cpf");
            String phone = (String) dados.get("phone");

            Client holder = new Client(name, cpf, phone);

            int num = (int) dados.get("number");
            int agency = (int) dados.get("agency");
            double initialBalance = (double) dados.getOrDefault("balance", 0.0);
            String type = (String) dados.get("type");

            Account newAccount;

            if ("CORRENTE".equalsIgnoreCase(type) || "CHECKING".equalsIgnoreCase(type)) {
                  CheckingAccount checking = new CheckingAccount(num, agency, initialBalance, holder);
                  if (dados.containsKey("limit")) {
                        checking.setLimit((Double) dados.get("limit"));
                  }
                  newAccount = checking;
            } else if ("POUPANCA".equalsIgnoreCase(type) || "SAVINGS".equalsIgnoreCase(type)) {
                  SavingsAccount savings = new SavingsAccount(num, agency, initialBalance, holder);
                  if (dados.containsKey("taxaRendimento")) {
                        savings.setYieldRate((Double) dados.get("taxaRendimento"));
                  }
                  newAccount = savings;
            } else {
                  throw new IllegalArgumentException("Invalid Account type: " + type);
            }

            repository.save(newAccount);
      }

      public boolean doWhitdraw(int num, double val) {
            Account account = repository.searchNum(num);
            if (account == null) {
                  System.out.println("Account number " + num + " not found.");
                  return false;
            }

            try {
                  account.whitDraw(val);
                  return true;
            } catch (BankException e) {
                  System.out.println("Error trying to make whitdraw: " + e.getMessage());
                  return false;
            }
      }

      public void doDeposit(int num, double val) {
            Account account = repository.searchNum(num);
            if (account == null) {
                  System.out.println("Account number " + num + " not found.");
                  return;
            }

            account.deposit(val);
      }
}
