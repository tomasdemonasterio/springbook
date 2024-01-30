package com.set.springbook.controller;

import com.set.springbook.model.Account;
import com.set.springbook.model.AccountDto;
import com.set.springbook.model.AccountRepository;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public List<AccountDto> list() {
        Pageable pageable = PageRequest.of(0, 10);
        return accountRepository.findAll(pageable).toList().stream().map(this::toDto).toList();
    }

    public void add(String username, String password) {
        Account user = new Account();
        user.setUsername(username);
        user.setPassword(this.passwordEncoder.encode(password));
        user.setAuthorities(List.of("ROLE_USER"));
        accountRepository.save(user);
    }

    public AccountDto getUser(Long id) {
        if (accountRepository.findById(id).isPresent()) {
            return toDto(accountRepository.findById(id).get());
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

    private Account toEntity(AccountDto accountdto) {
        Converter<List<Long>, List<Account>> listIdToListAccount = c -> {
            List<Long> ids = c.getSource().stream().toList();
            List<Account> accounts = new ArrayList<>();
            if (!ids.isEmpty()) {
                for (Long id : ids) {
                    Account account = accountRepository.findById(id)
                            .isPresent() ? accountRepository.findById(id).get() : null;
                    accounts.add(account);
                }
            }
            return accounts;
        };
        modelMapper.typeMap(AccountDto.class, Account.class)
                .addMappings(mapper -> mapper.using(listIdToListAccount).map(AccountDto::getFollowers, Account::setFollowers))
                .addMappings(mapper -> mapper.using(listIdToListAccount).map(AccountDto::getFollowing, Account::setFollowing));

        return modelMapper.map(accountdto, Account.class);
    }

    private AccountDto toDto(Account account) {
        Converter<List<Account>, List<Long>> listAccountToListId = c -> {
            List<Account> accounts = c.getSource().stream().toList();
            List<Long> ids = new ArrayList<>();
            if (!accounts.isEmpty()) {
                for (Account accountS : accounts) {
                    ids.add(accountS.getId());
                }
            }
            return ids;
        };
        modelMapper.typeMap(Account.class, AccountDto.class)
                .addMappings(mapper -> mapper.using(listAccountToListId).map(Account::getFollowers, AccountDto::setFollowers))
                .addMappings(mapper -> mapper.using(listAccountToListId).map(Account::getFollowing, AccountDto::setFollowing));
        return modelMapper.map(account, AccountDto.class);
    }
}

