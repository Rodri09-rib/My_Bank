package application;

import domain.Account;
import domain.SavingsAccount;
import service.BankException;
import service.BankService;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Program {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BankService bankService = new BankService();
        boolean run = true;

        while (run) {
            try {
                System.out.println("-------------------------------");
                System.out.println("           Banco-RRF           ");
                System.out.println("-------------------------------");
                System.out.println("  MENU ");
                System.out.println("1 - Cadastrar conta corrente.");
                System.out.println("2 - Cadastrar conta poupança.");
                System.out.println("3 - Entrar na conta.");
                System.out.println("0 - Sair.");
                System.out.println(" ");

                int swt = readInt(sc, "Escolha uma opção: ");

                switch (swt) {
                    case 1 -> createCheckingAccount(sc, bankService);
                    case 2 -> createSavingsAccount(sc, bankService);
                    case 3 -> enterAccount(sc, bankService);
                    case 0 -> {
                        System.out.println("Programa encerrado.");
                        run = false;
                    }
                    default -> System.out.println("Opção inválida.");
                }
            } catch (BankException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }

        sc.close();
    }

    private static void createCheckingAccount(Scanner sc, BankService bankService) {
        System.out.println("- Cadastro de conta corrente -");

        Map<String, Object> dados = baseData(sc);
        dados.put("type", "CORRENTE");
        dados.put("limit", readDouble(sc, "Informe o limite de saque: "));

        Account account = bankService.createAccount(dados);
        System.out.println("Conta criada e salva!");
        System.out.println(account);
    }

    private static void createSavingsAccount(Scanner sc, BankService bankService) {
        System.out.println("- Cadastro de conta poupança -");

        Map<String, Object> dados = baseData(sc);
        dados.put("type", "POUPANCA");
        dados.put("taxaRendimento", readDouble(sc, "Informe a taxa de rendimento: "));

        Account account = bankService.createAccount(dados);
        System.out.println("Conta criada e salva!");
        System.out.println(account);
    }

    private static Map<String, Object> baseData(Scanner sc) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("name", readLine(sc, "Informe o seu nome completo: "));
        dados.put("cpf", readLine(sc, "Informe seu CPF: "));
        dados.put("phone", readLine(sc, "Informe seu número de telefone: "));
        dados.put("number", readInt(sc, "Digite o número para sua conta: "));
        dados.put("agency", readInt(sc, "Informe o número para sua agência: "));
        dados.put("balance", readDouble(sc, "Digite seu saldo inicial: "));
        return dados;
    }

    private static void enterAccount(Scanner sc, BankService bankService) {
        System.out.println("Entre em sua conta.");

        int n = readInt(sc, "Informe o número de sua conta: ");
        Account account = bankService.searchAccount(n);
        if (account == null) {
            System.out.println("Conta " + n + " não encontrada.");
            return;
        }

        System.out.println(account);

        boolean inside = true;
        while (inside) {
            try {
                System.out.println("-------------------------------");
                System.out.println("  CONTA " + account.getNum());
                System.out.println("1 - Depositar.");
                System.out.println("2 - Sacar.");
                System.out.println("3 - Ver dados da conta.");
                System.out.println("4 - Aplicar rendimento.");
                System.out.println("0 - Voltar ao menu.");
                System.out.println(" ");

                int option = readInt(sc, "Escolha uma opção: ");
                switch (option) {
                    case 1 -> {
                        double value = readDouble(sc, "Informe o valor do depósito: ");
                        bankService.doDeposit(account.getNum(), value);
                        System.out.printf("Depósito de %.2f realizado.%n", value);
                        System.out.println(account);
                    }
                    case 2 -> {
                        double value = readDouble(sc, "Informe o valor do saque: ");
                        if (bankService.doWithdraw(account.getNum(), value)) {
                            System.out.printf("Saque de %.2f realizado.%n", value);
                            System.out.println(account);
                        }
                    }
                    case 3 -> System.out.println(account);
                    case 4 -> applyEarnings(account);
                    case 0 -> inside = false;
                    default -> System.out.println("Opção inválida.");
                }
            } catch (BankException e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private static void applyEarnings(Account account) {
        if (!(account instanceof SavingsAccount savings)) {
            System.out.println("Rendimento disponível apenas em conta poupança.");
            return;
        }
        double earnings = savings.applyEarnings();
        System.out.printf("Rendimento aplicado: %.2f%n", earnings);
        System.out.println(account);
    }

    static String readLine(Scanner sc, String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    static int readInt(Scanner sc, String prompt) {
        while (true) {
            String text = readLine(sc, prompt);
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
            }
        }
    }

    static double readDouble(Scanner sc, String prompt) {
        while (true) {
            String text = readLine(sc, prompt).replace(',', '.');
            try {
                return Double.parseDouble(text);
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Digite um número, por exemplo 1000,50.");
            }
        }
    }
}