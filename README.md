# My_Bank

My Bank é um projeto realizado em Java, que simula um aplicativo bancário, onde as interações ocorrem através do terminal. Os dados ficam apenas em memória: ao encerrar o programa, tudo é perdido.

## Como executar

Requer JDK 17+ e Maven.

```bash
mvn compile        # compila
mvn test           # roda os testes
mvn exec:java      # executa o menu no terminal
```

Também é possível rodar direto pela IDE: abra `application.Program` e execute o método `main`.

## Menu

| Opção | Ação |
| --- | --- |
| 1 | Cadastrar conta corrente |
| 2 | Cadastrar conta poupança |
| 3 | Entrar em uma conta pelo número |
| 0 | Sair |

Dentro da conta: `1` depositar, `2` sacar, `3` ver dados, `4` aplicar rendimento (só poupança), `0` voltar.

## Regras

- Número de conta e de agência devem ser maiores que zero, e o número da conta é único.
- O saldo inicial não pode ser negativo.
- Depósito e saque precisam ser maiores que zero.
- O saque nunca pode passar do saldo.
- Na conta corrente, o saque também não pode passar do limite cadastrado. Se a conta foi criada sem limite, não há restrição de valor além do saldo.
- Conta poupança tem taxa de rendimento: aplicar rendimento soma `saldo * taxa` ao saldo.

## Estrutura

```
src/
  application/Program.java          menu do terminal e leitura de entrada
  service/BankService.java          regras de negócio e validações
  service/BankException.java        exceção de negócio (RuntimeException)
  repository/RepositoryAccount.java armazenamento em memória
  domain/Account.java               conta abstrata (número, agência, saldo, titular)
  domain/CheckingAccount.java       conta corrente, com limite de saque
  domain/SavingsAccount.java        conta poupança, com rendimento
  domain/Client.java                titular
src/test/java/                      testes JUnit 5
```

O `BankService` é quem decide o que é erro de negócio e o `Program` é quem exibe as mensagens: nenhuma camada de domínio ou repositório imprime no console.