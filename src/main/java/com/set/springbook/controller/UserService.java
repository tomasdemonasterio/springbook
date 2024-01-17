package com.set.springbook.controller;

import com.set.springbook.model.UserTable;
import com.set.springbook.model.UserRepository;
import jakarta.transaction.Transactional;
import org.apache.catalina.Authenticator;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;


@Controller
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserTable> list() {
        Pageable pageable = PageRequest.of(0, 10);

        return userRepository.findAll(pageable).toList();
    }

    public void add(String username, String password) {
        UserTable user = new UserTable();
        user.setUsername(username);
        user.setPassword(this.passwordEncoder.encode(password));
        user.setAuthorities(List.of("ROLE_USER"));
        userRepository.save(user);
    }

    public List<UserTable> ToFollowList(UserTable user) {
        List<UserTable> users = userRepository.findAll();
        users.remove(user);
        for(UserTable u : user.getFollowing()) {
            users.remove(u);
        }
        return users;
    }

    public UserTable getUser(Long id) {
        if (userRepository.findById(id).isPresent()) {
            return userRepository.findById(id).get();
        }
        return null;
    }

    @PreAuthorize("#userId == authentication.principal.id")
    public void followTo(Long followId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        UserTable user = userRepository.findByUsername(((UserDetails) auth.getPrincipal()).getUsername());
        Optional<UserTable> followUser = userRepository.findById(followId);
        if (followUser.isPresent()
                && !user.getFollowing().contains(followUser.get())
                && !followUser.get().getFollowers().contains(user)
                && !user.equals(followUser.get())) {
            user.getFollowing().add(followUser.get());
            followUser.get().getFollowers().add(user);
            userRepository.save(followUser.get());
            userRepository.save(user);
        }
    }
}
