package com.set.springbook.controller;

import com.set.springbook.model.UserDto;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/users/{id}")
    public String userId(@PathVariable Long id, Model model) {
        UserDto user = userService.getUser(id);
        if (user == null) {
            return "redirect:/users";
        }
        model.addAttribute("user", user);
        return "user";
    }

    @Transactional
    @PostMapping("/users/{id}")
    public String follow(@PathVariable Long id, @RequestParam Long followId) {

        this.userService.followTo(followId);

        return "redirect:/users/{id}";
    }
}
