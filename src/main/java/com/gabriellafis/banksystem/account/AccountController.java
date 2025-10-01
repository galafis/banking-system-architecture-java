package com.gabriellafis.banksystem.account;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @GetMapping
    public ResponseEntity<List<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.getAllAccounts());
    }

    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> getAccountByNumber(@PathVariable String accountNumber) {
        return ResponseEntity.ok(accountService.getAccountByNumber(accountNumber));
    }

    @PutMapping("/{accountNumber}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable String accountNumber, @RequestBody Map<String, Double> request) {
        Double amount = request.get("amount");
        if (amount == null || amount <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(accountService.deposit(accountNumber, amount));
    }

    @PutMapping("/{accountNumber}/withdraw")
    public ResponseEntity<Account> withdraw(@PathVariable String accountNumber, @RequestBody Map<String, Double> request) {
        Double amount = request.get("amount");
        if (amount == null || amount <= 0) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(accountService.withdraw(accountNumber, amount));
    }

    @PutMapping("/transfer")
    public ResponseEntity<Void> transfer(@RequestBody Map<String, Object> request) {
        String fromAccountNumber = (String) request.get("fromAccountNumber");
        String toAccountNumber = (String) request.get("toAccountNumber");
        Double amount = (Double) request.get("amount");

        if (fromAccountNumber == null || toAccountNumber == null || amount == null || amount <= 0) {
            return ResponseEntity.badRequest().build();
        }
        accountService.transfer(fromAccountNumber, toAccountNumber, amount);
        return ResponseEntity.ok().build();
    }
}

