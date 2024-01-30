package com.set.springbook.controller;

import com.set.springbook.model.User;
import com.set.springbook.model.UserDto;
import com.set.springbook.model.UserRepository;

import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ModelMapper modelMapper;

    public List<UserDto> list() {
        Pageable pageable = PageRequest.of(0, 10);
        return userRepository.findAll(pageable).toList().stream().map(this::toDto).toList();
    }

    public void add(String username, String password) {
        User user = new User();
        user.setUsername(username);
        user.setPassword(this.passwordEncoder.encode(password));
        user.setAuthorities(List.of("ROLE_USER"));
        userRepository.save(user);
    }

    public UserDto getUser(Long id) {
        if (userRepository.findById(id).isPresent()) {
            return toDto(userRepository.findById(id).get());
        }
        return null;
    }

    @PreAuthorize("#userId == authentication.principal.id")
    public void followTo(Long followId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        User user = userRepository.findByUsername(((UserDetails) auth.getPrincipal()).getUsername());
        Optional<User> followUser = userRepository.findById(followId);
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

    private User toEntity(UserDto userDto) {
        Converter<List<Long>, List<User>> listIdToListUser = c -> {
            List<Long> ids = c.getSource().stream().toList();
            List<User> users = new ArrayList<>();
            if (!ids.isEmpty()) {
                for (Long id : ids) {
                    User user = userRepository.findById(id)
                            .isPresent() ? userRepository.findById(id).get() : null;
                    users.add(user);
                }
            }
            return users;
        };
        modelMapper.typeMap(UserDto.class, User.class)
                .addMappings(mapper -> mapper.using(listIdToListUser).map(UserDto::getFollowers, User::setFollowers))
                .addMappings(mapper -> mapper.using(listIdToListUser).map(UserDto::getFollowing, User::setFollowing));

        return modelMapper.map(userDto, User.class);
    }

    private UserDto toDto(User user) {
        Converter<List<User>, List<Long>> listUserToListId = c -> {
            List<User> users = c.getSource().stream().toList();
            List<Long> ids = new ArrayList<>();
            if (!users.isEmpty()) {
                for (User userSource : users) {
                    ids.add(userSource.getId());
                }
            }
            return ids;
        };
        modelMapper.typeMap(User.class, UserDto.class)
                .addMappings(mapper -> mapper.using(listUserToListId).map(User::getFollowers, UserDto::setFollowers))
                .addMappings(mapper -> mapper.using(listUserToListId).map(User::getFollowing, UserDto::setFollowing));
        return modelMapper.map(user, UserDto.class);
    }
}

