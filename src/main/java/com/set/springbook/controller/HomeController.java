package com.set.springbook.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @Autowired
    private AccountService accountService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("accounts", this.accountService.list());

        return "home";
    }
}
