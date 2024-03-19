package com.set.springbook.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class UserControllerRestApi {
    @Autowired
    private UserService userService;

    @GetMapping("/api/users")
    public Object getUsers(@RequestParam(required = false) Long id) {
        if (id != null) {
            return userService.getUser(id);
        }
        return userService.list();
    }

    @Transactional
    @PostMapping("api/users/{id}")
    public String follow(@PathVariable Long id, @RequestParam Long followId) {

        this.userService.followTo(followId);

        return "redirect:/users/{id}";
    }
}
