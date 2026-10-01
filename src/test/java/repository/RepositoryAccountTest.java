package repository;

import domain.Account;
import domain.CheckingAccount;
import domain.Client;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RepositoryAccountTest {

    private RepositoryAccount repository;

    @BeforeEach
    void setUp() {
        this.repository = new RepositoryAccount();
    }

    private Account checking(int num) {
        return new CheckingAccount(num, 1, 100.0, new Client("Ana", "111", "911"));
    }

    @Test
    void saveStoresAccount() {
        assertTrue(this.repository.save(checking(10)));
        assertNotNull(this.repository.searchNum(10));
        assertEquals(1, this.repository.listAll().size());
    }

    @Test
    void saveRejectsDuplicatedNumber() {
        assertTrue(this.repository.save(checking(10)));
        assertFalse(this.repository.save(checking(10)));
        assertEquals(1, this.repository.listAll().size());
    }

    @Test
    void searchNumReturnsNullWhenNotFound() {
        assertNull(this.repository.searchNum(999));
    }

    @Test
    void deleteRemovesAccount() {
        this.repository.save(checking(10));
        assertTrue(this.repository.delete(10));
        assertNull(this.repository.searchNum(10));
        assertFalse(this.repository.delete(10));
    }

    @Test
    void listAllReturnsImmutableCopy() {
        this.repository.save(checking(10));
        List<Account> all = this.repository.listAll();
        assertThrows(UnsupportedOperationException.class, () -> all.add(checking(11)));
    }

    @Test
    void saveRejectsNullAccount() {
        assertThrows(IllegalArgumentException.class, () -> this.repository.save(null));
    }
}