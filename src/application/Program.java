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
        System.out.print("Escolha uma opção: ");

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

                System.out.print("Digite um número para sua conta: ");
                int num = sc.nextInt();

                System.out.print("Informe um número para sua agência: ");
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

            default:
                System.out.println("Opção inválida.");
                break;
        }

        sc.close();
    }
}