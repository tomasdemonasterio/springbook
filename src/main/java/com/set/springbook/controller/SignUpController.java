package com.set.springbook.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {
    @Autowired
    private UserService userService;

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }

    @Transactional
    @PostMapping("/signup")
    public String signupPost(@RequestParam String username, @RequestParam String password) {
        this.userService.add(username, password);
        return "redirect:/login";
    }
}
