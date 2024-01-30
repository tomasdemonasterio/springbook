package com.set.springbook.controller;

import com.set.springbook.model.Account;
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

    @GetMapping("/users/{id}")
    public String userId(@PathVariable Long id, Model model) {
        Account user = accountService.getUser(id);
        if (user == null) {
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        model.addAttribute("users", accountService.ToFollowList(user));
        return "account";
    }

    @Transactional
    @PostMapping("/users/{id}")
    public String follow(@RequestParam Long followId) {

        this.accountService.followTo(followId);

        return "redirect:/users/{id}";
    }
}
