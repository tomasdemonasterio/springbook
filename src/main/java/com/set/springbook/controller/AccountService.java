package com.set.springbook.controller;

import com.set.springbook.model.Account;
import com.set.springbook.model.AccountRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;


@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<Account> list() {
        Pageable pageable = PageRequest.of(0, 10);

        return accountRepository.findAll(pageable).toList();
    }

    public void add(String username, String password) {
        Account user = new Account();
        user.setUsername(username);
        user.setPassword(this.passwordEncoder.encode(password));
        user.setAuthorities(List.of("ROLE_USER"));
        accountRepository.save(user);
    }

    public List<Account> ToFollowList(Account user) {
        List<Account> users = accountRepository.findAll();
        users.remove(user);
        for (Account u : user.getFollowing()) {
            users.remove(u);
        }
        return users;
    }

    public Account getUser(Long id) {
        if (accountRepository.findById(id).isPresent()) {
            return accountRepository.findById(id).get();
        }
        return null;
    }

    @PreAuthorize("#userId == authentication.principal.id")
    public void followTo(Long followId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        Account user = accountRepository.findByUsername(((UserDetails) auth.getPrincipal()).getUsername());
        Optional<Account> followUser = accountRepository.findById(followId);
        if (followUser.isPresent()
                && !user.getFollowing().contains(followUser.get())
                && !followUser.get().getFollowers().contains(user)
                && !user.equals(followUser.get())) {
            user.getFollowing().add(followUser.get());
            followUser.get().getFollowers().add(user);
            accountRepository.save(followUser.get());
            accountRepository.save(user);
        }
    }
}
