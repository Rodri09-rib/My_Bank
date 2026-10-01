package service;

import domain.Account;
import domain.CheckingAccount;
import domain.SavingsAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BankServiceTest {

    private BankService bankService;

    @BeforeEach
    void setUp() {
        this.bankService = new BankService();
    }

    private Map<String, Object> baseData(int num) {
        Map<String, Object> dados = new HashMap<>();
        dados.put("name", "Ana Silva");
        dados.put("cpf", "12345678900");
        dados.put("phone", "911111111");
        dados.put("number", num);
        dados.put("agency", 1);
        dados.put("balance", 500.0);
        return dados;
    }

    private Map<String, Object> checkingData(int num) {
        Map<String, Object> dados = baseData(num);
        dados.put("type", "CORRENTE");
        dados.put("limit", 300.0);
        return dados;
    }

    private Map<String, Object> savingsData(int num) {
        Map<String, Object> dados = baseData(num);
        dados.put("type", "POUPANCA");
        dados.put("taxaRendimento", 0.01);
        return dados;
    }

    @Test
    void createCheckingAccountKeepsInitialBalance() {
        Account account = this.bankService.createAccount(checkingData(100));

        CheckingAccount checking = assertInstanceOf(CheckingAccount.class, account);
        assertEquals(100, checking.getNum());
        assertEquals(1, checking.getAgency());
        assertEquals(500.0, checking.getBalance());
        assertEquals(300.0, checking.getLimit());
        assertEquals("Ana Silva", checking.getHolder().getName());
    }

    @Test
    void createSavingsAccountKeepsYieldRate() {
        Account account = this.bankService.createAccount(savingsData(200));

        SavingsAccount savings = assertInstanceOf(SavingsAccount.class, account);
        assertEquals(500.0, savings.getBalance());
        assertEquals(0.01, savings.getYieldRate());
    }

    @Test
    void createAccountRejectsDuplicatedNumber() {
        this.bankService.createAccount(checkingData(100));
        BankException e = assertThrows(BankException.class,
                () -> this.bankService.createAccount(savingsData(100)));
        assertTrue(e.getMessage().contains("100"));
    }

    @Test
    void createAccountRejectsInvalidType() {
        Map<String, Object> dados = baseData(100);
        dados.put("type", "INVESTIMENTO");
        assertThrows(BankException.class, () -> this.bankService.createAccount(dados));
    }

    @Test
    void createAccountRejectsMissingField() {
        Map<String, Object> dados = checkingData(100);
        dados.remove("balance");
        assertThrows(BankException.class, () -> this.bankService.createAccount(dados));
    }

    @Test
    void createAccountRejectsBlankName() {
        Map<String, Object> dados = checkingData(100);
        dados.put("name", "   ");
        assertThrows(BankException.class, () -> this.bankService.createAccount(dados));
    }

    @Test
    void createAccountRejectsNegativeInitialBalance() {
        Map<String, Object> dados = checkingData(100);
        dados.put("balance", -10.0);
        assertThrows(BankException.class, () -> this.bankService.createAccount(dados));
    }

    @Test
    void searchAccountReturnsCreatedAccount() {
        this.bankService.createAccount(checkingData(100));
        assertNotNull(this.bankService.searchAccount(100));
        assertNull(this.bankService.searchAccount(777));
    }

    @Test
    void doDepositAddsToBalance() {
        this.bankService.createAccount(checkingData(100));

        assertTrue(this.bankService.doDeposit(100, 200.0));
        assertEquals(700.0, this.bankService.searchAccount(100).getBalance());
    }

    @Test
    void doWithdrawRemovesFromBalance() {
        this.bankService.createAccount(checkingData(100));

        assertTrue(this.bankService.doWithdraw(100, 200.0));
        assertEquals(300.0, this.bankService.searchAccount(100).getBalance());
    }

    @Test
    void doWithdrawRefusesValueAboveLimit() {
        this.bankService.createAccount(checkingData(100));

        assertThrows(BankException.class, () -> this.bankService.doWithdraw(100, 400.0));
        assertEquals(500.0, this.bankService.searchAccount(100).getBalance());
    }

    @Test
    void doWithdrawRefusesValueAboveBalance() {
        this.bankService.createAccount(savingsData(200));

        assertThrows(BankException.class, () -> this.bankService.doWithdraw(200, 900.0));
        assertEquals(500.0, this.bankService.searchAccount(200).getBalance());
    }

    @Test
    void doWithdrawRefusesNonPositiveValue() {
        this.bankService.createAccount(checkingData(100));
        assertThrows(BankException.class, () -> this.bankService.doWithdraw(100, 0.0));
        assertThrows(BankException.class, () -> this.bankService.doWithdraw(100, -50.0));
    }

    @Test
    void operationsReturnFalseWhenAccountNotFound() {
        assertFalse(this.bankService.doDeposit(404, 10.0));
        assertFalse(this.bankService.doWithdraw(404, 10.0));
    }

    @Test
    void doWithdrawWithoutLimitConfiguredWorks() {
        Map<String, Object> dados = baseData(300);
        dados.put("type", "CORRENTE");
        this.bankService.createAccount(dados);

        assertTrue(this.bankService.doWithdraw(300, 450.0));
        assertEquals(50.0, this.bankService.searchAccount(300).getBalance());
    }

    @Test
    void applyEarningsIncreasesSavingsBalance() {
        this.bankService.createAccount(savingsData(200));

        SavingsAccount savings = (SavingsAccount) this.bankService.searchAccount(200);
        double earnings = savings.applyEarnings();

        assertEquals(5.0, earnings);
        assertEquals(505.0, savings.getBalance());
    }

    @Test
    void applyEarningsDoesNotFailOnEmptySavings() {
        Map<String, Object> dados = savingsData(400);
        dados.put("balance", 0.0);
        this.bankService.createAccount(dados);

        SavingsAccount savings = (SavingsAccount) this.bankService.searchAccount(400);
        assertEquals(0.0, savings.applyEarnings());
        assertEquals(0.0, savings.getBalance());
    }

    @Test
    void accountToStringContainsRelevantData() {
        String text = this.bankService.createAccount(checkingData(100)).toString();

        assertTrue(text.contains("100"));
        assertTrue(text.contains("Ana Silva"));
        assertTrue(text.contains("500.0"));
        assertTrue(text.contains("300.0"));
    }
}