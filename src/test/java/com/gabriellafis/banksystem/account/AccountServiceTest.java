package com.gabriellafis.banksystem.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    private Account account1;
    private Account account2;

    @BeforeEach
    void setUp() {
        account1 = new Account(1L, "12345", "Gabriel Lafis", 1000.00);
        account2 = new Account(2L, "67890", "Maria Silva", 500.00);
    }

    @Test
    void createAccount() {
        when(accountRepository.save(any(Account.class))).thenReturn(account1);
        Account createdAccount = accountService.createAccount(account1);
        assertNotNull(createdAccount);
        assertEquals("12345", createdAccount.getAccountNumber());
    }

    @Test
    void getAccountByNumber() {
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account1));
        Account foundAccount = accountService.getAccountByNumber("12345");
        assertNotNull(foundAccount);
        assertEquals("Gabriel Lafis", foundAccount.getAccountHolderName());
    }

    @Test
    void deposit() {
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account1));
        when(accountRepository.save(any(Account.class))).thenReturn(account1);

        Account updatedAccount = accountService.deposit("12345", 200.00);
        assertEquals(1200.00, updatedAccount.getBalance());
    }

    @Test
    void withdraw() {
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account1));
        when(accountRepository.save(any(Account.class))).thenReturn(account1);

        Account updatedAccount = accountService.withdraw("12345", 150.00);
        assertEquals(850.00, updatedAccount.getBalance());
    }

    @Test
    void withdrawInsufficientFunds() {
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account1));
        assertThrows(RuntimeException.class, () -> accountService.withdraw("12345", 1500.00));
    }

    @Test
    void transfer() {
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account1));
        when(accountRepository.findByAccountNumber("67890")).thenReturn(Optional.of(account2));
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(invocation -> invocation.getArgument(0)); // Return the argument passed to save

        accountService.transfer("12345", "67890", 100.00);

        assertEquals(900.00, account1.getBalance());
        assertEquals(600.00, account2.getBalance());
        verify(accountRepository, times(2)).save(any(Account.class));
    }

    @Test
    void transferInsufficientFunds() {
        when(accountRepository.findByAccountNumber("12345")).thenReturn(Optional.of(account1));
        when(accountRepository.findByAccountNumber("67890")).thenReturn(Optional.of(account2));

        assertThrows(RuntimeException.class, () -> accountService.transfer("12345", "67890", 1500.00));
    }
}

