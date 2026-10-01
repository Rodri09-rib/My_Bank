# Projeto My Bank.
[![Licença MIT](https://img.shields.io/badge/License-MIT-blue.svg)](https://github.com/Rodri09-rib/My_Bank/blob/main/LICENSE)

# Sobre o projeto


Este projeto consiste num sistema bancário didático desenvolvido 100% em Java puro, sem a utilização de frameworks ou bases de dados externas. O seu principal propósito é ensinar na prática os pilares da Orientação a Objetos, como o encapsulamento, a herança e o polimorfismo.

O projeto encontra-se organizado numa arquitetura de camadas para evitar "código esparguete" e facilitar a manutenção.

My Bank simula um aplicativo bancário cujas interações ocorrem através do terminal. Os dados ficam apenas em memória: ao encerrar o programa, tudo é perdido.

## Modelo conceitual
![]()<img width="2816" height="1536" alt="mybank" src="https://github.com/user-attachments/assets/d2bdd5ce-ac88-44d2-bb4a-62b02c4493bf" />




# Como executar

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
  main/java/application/Program.java          menu do terminal e leitura de entrada
  main/java/service/BankService.java          regras de negócio e validações
  main/java/service/BankException.java        exceção de negócio (RuntimeException)
  main/java/repository/RepositoryAccount.java armazenamento em memória
  main/java/domain/Account.java               conta abstrata (número, agência, saldo, titular)
  main/java/domain/CheckingAccount.java       conta corrente, com limite de saque
  main/java/domain/SavingsAccount.java        conta poupança, com rendimento
  main/java/domain/Client.java                titular
  test/java/                                   testes JUnit 5
```

O `BankService` é quem decide o que é erro de negócio e o `Program` é quem exibe as mensagens: nenhuma camada de domínio ou repositório imprime no console.

# Tecnologias utilizadas
## Back end
- Java 21 ☕

# Autor

Rodrigo Ribeiro Ferreira

https://www.linkedin.com/in/rodrigo-ribeiro-abbb713aa?utm_source=share_via&utm_content=profile&utm_medium=member_ios