package com.demo.accountservice.controller;

import com.demo.accountservice.dto.CreateAccountDto;
import com.demo.accountservice.model.Account;
import com.demo.accountservice.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public Account createAccount(@RequestBody CreateAccountDto dto) {
        return accountService.createAccount(dto);
    }

    @GetMapping
    public List<Account> getAccounts() {
        return accountService.findAll();
    }

}
