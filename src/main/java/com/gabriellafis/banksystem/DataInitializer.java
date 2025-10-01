package com.gabriellafis.banksystem;

import com.gabriellafis.banksystem.account.Account;
import com.gabriellafis.banksystem.account.AccountRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final AccountRepository accountRepository;

    public DataInitializer(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (accountRepository.count() == 0) {
            accountRepository.save(new Account(null, "1001", "Gabriel Lafis", 1500.00));
            accountRepository.save(new Account(null, "1002", "Maria Silva", 2500.00));
            accountRepository.save(new Account(null, "1003", "João Souza", 500.00));
            System.out.println("Dados de exemplo inicializados.");
        }
    }
}

