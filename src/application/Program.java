package application;

import service.BankService;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Program {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        BankService bankService = new BankService();

        System.out.println("-------------------------------");
        System.out.println("           Banco-RRF           ");
        System.out.println("-------------------------------");
        System.out.println("  MENU ");
        System.out.println("1 - Cadastrar conta corrente.");
        System.out.println("2 - Cadastrar conta poupança.");
        System.out.println(" ");
        System.out.print("Escolha uma opção: ");
        System.out.println(" ");

        int swt = sc.nextInt();
        sc.nextLine(); // Limpa o buffer do teclado após ler o inteiro

        switch (swt) {
            case 1:
                System.out.println("- Cadastro de conta corrente -");

                System.out.print("Informe o seu nome completo: ");
                String holder = sc.nextLine();

                System.out.print("Informe seu CPF: ");
                String cpf = sc.nextLine();

                System.out.print("Informe seu número de telefone: ");
                String phone = sc.nextLine();

                System.out.print("Digite o número para sua conta: ");
                int num = sc.nextInt();

                System.out.print("Informe o número para sua agência: ");
                int agency = sc.nextInt();

                System.out.print("Digite seu saldo inicial: ");
                double balance = sc.nextDouble();

                // Monta o Map esperado pelo método createAccount
                Map<String, Object> dados = new HashMap<>();
                dados.put("name", holder);
                dados.put("cpf", cpf);
                dados.put("phone", phone);
                dados.put("number", num);
                dados.put("agency", agency);
                dados.put("balance", balance);
                dados.put("type", "CORRENTE");

                bankService.createAccount(dados);
                System.out.println("Conta criada e salva!");
                break;

            case 2:
                System.out.println(" - Cadastro de conta poupança -");

                System.out.println("Informe o seu nome completo: ");
                String holder1 = sc.nextLine();

                System.out.println("Informe seu CPF: ");
                String cpf1 = sc.nextLine();

                System.out.println("Informe seu número de telefone: ");
                String phone1 = sc.nextLine();

                System.out.println("Digite o número de sua conta: ");
                int num1 = sc.nextInt();

                System.out.println("Informe o número de sua agência: ");
                int agency1 = sc.nextInt();

                System.out.println("Digite seu saldo inicial: ");
                double balance1 = sc.nextDouble();

                System.out.println("Digite a taxa de rendimento: ");
                double yeldRate = sc.nextDouble();

                Map<String, Object> dados1 = new HashMap<>();
                dados1.put("name", holder1);
                dados1.put("cpf", cpf1);
                dados1.put("phone", phone1);
                dados1.put("number", num1);
                dados1.put("agency", agency1);
                dados1.put("balance", balance1);
                dados1.put("taxaRendimento", yeldRate);
                dados1.put("type", "POUPANCA");

                bankService.createAccount(dados1);
                System.out.println("Conta criada e salva!");
                break;

            default:
                System.out.println("Opção inválida.");
                break;
        }

        sc.close();
    }
}