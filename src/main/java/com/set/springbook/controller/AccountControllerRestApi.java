package com.set.springbook.controller;

import com.set.springbook.model.AccountDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AccountControllerRestApi {
    @Autowired
    private AccountService accountService;

    @GetMapping("/api/accounts")
    public List<AccountDto> getAccounts() {
        return accountService.list();
    }

    @GetMapping("/api/account/{id}")
    public AccountDto getAccount(@PathVariable Long id) {
        return accountService.getUser(id);
    }
}
