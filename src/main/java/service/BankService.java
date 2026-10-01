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

    public Account createAccount(Map<String, Object> dados) {
        Client holder = new Client(
                requireText(dados, "name"),
                requireText(dados, "cpf"),
                requireText(dados, "phone")
        );

        int num = requireInt(dados, "number");
        int agency = requireInt(dados, "agency");
        double initialBalance = requireDouble(dados, "balance");
        String type = requireText(dados, "type");

        Account newAccount;
        if ("CORRENTE".equalsIgnoreCase(type) || "CHECKING".equalsIgnoreCase(type)) {
            CheckingAccount checking = new CheckingAccount(num, agency, initialBalance, holder);
            if (dados.containsKey("limit")) {
                checking.setLimit(requireDouble(dados, "limit"));
            }
            newAccount = checking;
        } else if ("POUPANCA".equalsIgnoreCase(type) || "SAVINGS".equalsIgnoreCase(type)) {
            SavingsAccount savings = new SavingsAccount(num, agency, initialBalance, holder);
            if (dados.containsKey("taxaRendimento")) {
                savings.setYieldRate(requireDouble(dados, "taxaRendimento"));
            }
            newAccount = savings;
        } else {
            throw new BankException("Tipo de conta inválido: " + type);
        }

        if (!this.repository.save(newAccount)) {
            throw new BankException("Já existe uma conta com o número " + num + ".");
        }
        return newAccount;
    }

    public Account searchAccount(int num) {
        return this.repository.searchNum(num);
    }

    public boolean doDeposit(int num, double value) {
        Account account = this.repository.searchNum(num);
        if (account == null) {
            return false;
        }
        account.deposit(value);
        return true;
    }

    public boolean doWithdraw(int num, double value) {
        Account account = this.repository.searchNum(num);
        if (account == null) {
            return false;
        }
        account.whitDraw(value);
        return true;
    }

    private static String requireText(Map<String, Object> dados, String key) {
        Object value = dados.get(key);
        if (!(value instanceof String text) || text.isBlank()) {
            throw new BankException("Campo obrigatório ausente ou vazio: " + key);
        }
        return text.trim();
    }

    private static int requireInt(Map<String, Object> dados, String key) {
        Object value = dados.get(key);
        if (!(value instanceof Integer number)) {
            throw new BankException("Campo obrigatório ausente ou inválido: " + key);
        }
        return number;
    }

    private static double requireDouble(Map<String, Object> dados, String key) {
        Object value = dados.get(key);
        if (!(value instanceof Double number)) {
            throw new BankException("Campo obrigatório ausente ou inválido: " + key);
        }
        return number;
    }
}