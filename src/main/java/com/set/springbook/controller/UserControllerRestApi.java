package com.set.springbook.controller;

import com.set.springbook.model.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class UserControllerRestApi {
    @Autowired
    private UserService userService;

    @GetMapping("/api/users")
    public List<UserDto> getUsers() {
        return userService.list();
    }

    @GetMapping("/api/user/{id}")
    public UserDto getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }
}
