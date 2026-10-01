<div align="center">

# My Bank

**Simulador bancário em Java puro, com dados apenas em memória**

[![Licença MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://github.com/Rodri09-rib/My_Bank/blob/main/LICENSE)
[![Java 17](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.org/)
[![Maven](https://img.shields.io/badge/Maven-3.9-C71A36.svg)](https://maven.apache.org/)
[![JUnit 5](https://img.shields.io/badge/JUnit-5.10.2-25A162.svg)](https://junit.org/junit5/)

</div>

## Sobre

O My Bank é um sistema bancário didático escrito **100% em Java puro**, sem frameworks e
sem base de dados. É a escolha deliberada: o objetivo não é o produto, é a prática dos
pilares da Orientação a Objetos — encapsulamento, herança e polimorfismo — com cada um
visível em algum ponto do código.

O sistema simula um aplicativo de banco cujas interações acontecem no **terminal**, por
menu. Os dados vivem num `ArrayList`: ao encerrar o programa, tudo se perde.

O projeto está organizado em **camadas** (`application`, `domain`, `repository`,
`service`) para evitar "código esparguete" e para deixar explícito quem decide o quê.

## Funcionalidades

**Contas**

- Cadastro de **conta corrente** e de **conta poupança**, com número, agência, saldo
  inicial e titular.
- Entrada na conta pelo número, a partir de um menu próprio com depositar, sacar, ver
  dados e aplicar rendimento.
- O `toString()` de cada conta é sobreposto para acrescentar o que só existe no tipo
  concreto: limite na corrente, taxa na poupança.

**Regras de negócio**

- Número de conta e de agência têm de ser maiores que zero, e o número da conta é único.
- O saldo inicial não pode ser negativo, e uma conta sem titular não é criada.
- Depósito e saque têm de ser maiores que zero.
- O saque nunca passa do saldo.
- Na **conta corrente**, o saque também não pode passar do limite cadastrado.
- Na **conta poupança**, aplicar rendimento soma `saldo * taxa` ao saldo.

**Console**

- `readInt` e `readDouble` repetem o pedido até receberem um valor válido: um `abc` no
  meio da entrada não derruba o programa.
- `readDouble` troca `,` por `.`, portanto aceita `1000,50` e `1000.50` — e funciona
  igual em `pt-BR` e em `en-US`. A leitura não depende do locale do sistema.

## Pilares de OO na prática

O projeto é o exercício, por isso vale apontar **onde** cada pilar aparece.

**Herança** — `CheckingAccount` e `SavingsAccount` estendem `Account`. Tudo o que as
duas têm em comum (número, agência, titular, saldo, depósito e saque) está no pai, escrito
uma vez só.

**Encapsulamento** — `balance` é `private` e nunca é escrito diretamente. Depositou,
sacou ou aplicou rendimento, o saldo passa por um método, e cada método valida o valor
antes de mexer nele. Quem chama não consegue deixar o saldo num estado impossível.

**Polimorfismo** — `BankService` não sabe qual o tipo da conta que está a operar:
procura pelo número, recebe um `Account` e chama `whitDraw(value)`. Quem executa é o tipo
real.

```java
Account account = this.repository.searchNum(num);   // pode ser corrente ou poupança
account.whitDraw(value);                            // cada tipo aplica a sua regra
```

E o `Account` é `abstract`: não existe uma "conta" sem ser corrente ou poupança, logo a
classe base não pode ser instanciada.

**O `instanceof` é a exceção, não a regra** — há um único uso de `instanceof` no projeto,
em `Program.applyEarnings`, que verifica se a conta aberta é poupança antes de aplicar
rendimento. É o *front-end* a precisar de saber o tipo; o serviço e o repositório nunca
perguntam.

## Decisões de projeto

**`BankException` é uma `RuntimeException` (unchecked).** As regras de domínio validadas
no construtor de `Account` são invariantes de qualquer objeto válido — não são condições
que o chamador tenha de antecipar. Fazem parte da construção do objeto, não do fluxo do
programa, portanto não é preciso `throws` em cada chamada.

**O repositório responde; o serviço decide.** `RepositoryAccount.save()` devolve `false`
quando o número já existe, em vez de lançar exceção. A decisão sobre se isso é um erro de
negócio cabe ao `BankService`, que a traduz em `BankException`. O repositório é
substituível por uma implementação com base de dados sem mudar uma linha do serviço.

**`listAll()` devolve uma cópia imutável** (`List.copyOf`). Quem recebe a lista não pode
alterar o estado interno do repositório a partir de uma referência partilhada.

**O limite da corrente é um `Double` anulável, não um `double` a zero.** É a diferença
entre "conta sem limite de saque" e "conta com limite de zero". Um `double` inicializado a
`0.0` tornaria as duas indistinguíveis e bloquearia qualquer saque numa conta sem limite.
Por isso `setLimit` só valida o valor quando ele é realmente informado, e o `toString()`
imprime `sem limite` no lugar do número.

**`applyEarnings()` reutiliza `deposit()`.** Em vez de somar `saldo + saldo * taxa`
direto no atributo, o rendimento passa pelo método de depósito. Assim o valor gerado
passa pela mesma validação de "maior que zero" de qualquer depósito, e uma conta de saldo
zero devolve `0` em vez de tentar depositar zero.

**Nenhuma camada de domínio ou repositório imprime no console.** O `BankService` decide o
que é erro de negócio; o `Program` é quem formata e exibe a mensagem. É o que permite
testar o serviço sem capturar `System.out`.

## Stack

| Camada | Tecnologias |
| --- | --- |
| Linguagem | Java 17 (compila com `maven.compiler.release=17`) |
| Interface | Console, com `Scanner` e menu em `System.out` |
| Persistência | `ArrayList<Account>` em memória — nada é gravado em disco |
| Exceções | `BankException`,unchecked, para erros de regra de negócio |
| Testes | JUnit 5.10.2, `assertThrows` / `assertInstanceOf` / `assertEquals` |
| Build | Maven 3.9+ (compiler, surefire e `exec-maven-plugin`) |

Sem dependências de runtime: o único artefacto do `pom.xml` é o JUnit, em âmbito `test`.

## Como executar

**Requisitos:** JDK 17+ e Maven 3.9+. Nada mais — não há base de dados a subir nem
variáveis de ambiente a carregar.

```bash
mvn compile        # compila
mvn test           # roda os testes
mvn exec:java      # executa o menu no terminal
```

O `exec-maven-plugin` já tem `application.Program` configurado como classe principal em
`pom.xml`, por isso o comando não leva argumentos. Também é possível rodar direto pela
IDE: abra `application.Program` e execute o `main`.

### Menu

| Opção | Ação |
| --- | --- |
| `1` | Cadastrar conta corrente |
| `2` | Cadastrar conta poupança |
| `3` | Entrar em uma conta pelo número |
| `0` | Sair |

Dentro da conta:

| Opção | Ação |
| --- | --- |
| `1` | Depositar |
| `2` | Sacar |
| `3` | Ver dados da conta |
| `4` | Aplicar rendimento (só poupança) |
| `0` | Voltar ao menu |

Erros de regra de negócio não derrubam o programa: são apanhados pelo `catch (BankException)`
e mostrados como `Erro: <mensagem>`, e o menu volta a aparecer.

## Testes

**31 testes**, todos a passar, sem falhas nem erros.

```bash
mvn clean test
```

A suíte cobre as três camadas com pesos diferentes:

| Classe | Testes | Foco |
| --- | --- | --- |
| `service.BankServiceTest` | 18 | Criação de contas, depósito, saque, rendimento e recusas |
| `application.ProgramInputTest` | 7 | Leitura de entrada: vírgula vs ponto, locale, linhas inválidas |
| `repository.RepositoryAccountTest` | 6 | Gravação, duplicados, remoção e cópia imutável |

O `BankServiceTest` fixa as regras pela via negativa, que é a que interessa: cada teste
de recusa confirma também o estado **depois** da recusa — o saldo tem de continuar
inalterado depois de um saque acima do limite, ou acima do saldo disponível. Um teste que
só verificasse a exceção passaria mesmo que o método mexesse no saldo antes de decidir
falhar.

O `ProgramInputTest` fixa a leitura de números com `Locale` trocado a meio do teste
(`pt-BR` e `en-US`), para travar a regressão de `readDouble` passar a depender do locale
do sistema.

## Estrutura

```
src/
├── main/java/
│   ├── application/
│   │   └── Program.java           menu do terminal e leitura de entrada (Scanner)
│   ├── domain/
│   │   ├── Account.java           conta abstrata: número, agência, saldo, titular
│   │   ├── CheckingAccount.java   corrente, com limite de saque anulável
│   │   ├── SavingsAccount.java    poupança, com taxa de rendimento
│   │   └── Client.java            titular (nome, CPF, telefone)
│   ├── repository/
│   │   └── RepositoryAccount.java armazenamento em memória (ArrayList)
│   └── service/
│       ├── BankService.java       regras de negócio e tradução de erros
│       └── BankException.java     exceção de negócio (RuntimeException)
└── test/java/                     testes JUnit 5
```

O sentido das dependências é de cima para baixo: `application` conhece `service`, que
conhece `domain` e `repository`. O `domain` não conhece ninguém. É essa inversão que
permite ao `BankService` receber um `RepositoryAccount` por construtor e ser testado sem
efeitos colaterais.

### Mapa de responsabilidades

| Camada | Responde por | Não faz |
| --- | --- | --- |
| `application` | Menus, prompts, formatação das mensagens | Regras de negócio |
| `service` | Validação, orquestração, tradução de erro | Impressão no console |
| `domain` | Invariantes de cada conta | Dependências externas |
| `repository` | Guardar, procurar, remover contas | Decidir o que é erro de negócio |

`RepositoryAccount.delete(int)` e `listAll()` existem e estão testados, mas não estão
ligados a nenhuma opção do menu — ficam disponíveis para quando o terminal ganhar
opção de gerir contas.

## Modelo conceitual

<img width="907" height="593" alt="Modelo conceitual do My Bank" src="https://github.com/user-attachments/assets/d2bdd5ce-ac88-44d2-bb4a-62b02c4493bf" />

## Limitações conhecidas

São limitações deliberadas de um projeto didático, e não esquecimentos:

- **Os dados não sobrevivem ao programa.** Não há persistência nenhuma.
- **`balance` é `double`, não `BigDecimal`.** `double` acumula erro de arredondamento em
  operações repetidas de soma e subtração. Num sistema bancário a sério seria
  `BigDecimal` ou valores em cêntimos; aqui a simplicidade do tipo serve ao objetivo
  pedagógico.
- **Sem autenticação.** A opção `3` entra na conta só com o número. Qualquer titular
  entra na conta de qualquer outro.
- **Não há concorrência.** Um `ArrayList` sem sincronização pressupõe um único utilizador.
- **O CPF é apenas guardado e mostrado.** Não é validado, nem verificada a unicidade.

## Autor

**Rodrigo Ribeiro Ferreira** — [LinkedIn](https://www.linkedin.com/in/rodrigo-ribeiro-abbb713aa?utm_source=share_via&utm_content=profile&utm_medium=member_ios)

## Licença

[MIT](LICENSE)