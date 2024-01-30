package com.set.springbook.controller;

import com.set.springbook.model.AccountDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/accounts/{id}")
    public String userId(@PathVariable Long id, Model model) {
        AccountDto user = accountService.getUser(id);
        if (user == null) {
            return "redirect:/accounts";
        }
        model.addAttribute("account", user);
        return "account";
    }

    @Transactional
    @PostMapping("/accounts/{id}")
    public String follow(@RequestParam Long followId) {

        this.accountService.followTo(followId);

        return "redirect:/accounts/{id}";
    }
}
