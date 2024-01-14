package com.set.springbook.controller;

import com.set.springbook.model.UserTable;
import com.set.springbook.model.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Service
@Controller
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/users")
    public String list(Model model) {
        model.addAttribute("users", userRepository.findAll());
        return "users";
    }

    @PostMapping("/users")
    public String add(@RequestParam String name) {
        UserTable userTable = new UserTable();
        userTable.setName(name);
        userRepository.save(userTable);
        return "redirect:/users";
    }

    @GetMapping("/users/{id}")
    public String show(@PathVariable Long id, Model model) {
        if (userRepository.findById(id).isPresent()) {
            UserTable user = userRepository.findById(id).get();
            List<UserTable> users = userRepository.findAll();
            users.remove(user);
            for(UserTable u : user.getFollowing()) {
                users.remove(u);
            }
            model.addAttribute("user", user);
            model.addAttribute("users", users);
            return "user";
        } else {
            return "redirect:/users";
        }
    }

    @Transactional
    @PostMapping("/users/{id}")
    public String delete(@PathVariable Long id, @RequestParam Long userToFollowId) {
        if(userRepository.findById(id).isPresent() && userRepository.findById(userToFollowId).isPresent()) {
            UserTable user = userRepository.findById(id).get();
            UserTable userToFollow = userRepository.findById(userToFollowId).get();
            user.getFollowing().add(userToFollow);
            userToFollow.getFollowers().add(user);
        }
        return "redirect:/users/{id}";
    }
}
