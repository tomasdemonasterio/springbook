package com.set.springbook.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Account extends AbstractPersistable<Long> {
    private String username;
    private String password;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorities;

    @ManyToMany
    @JoinTable(name = "user_followers"
            , joinColumns = @JoinColumn(name = "user_id")
            , inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private List<Account> followers;

    @ManyToMany(mappedBy = "followers")
    private List<Account> following;

}
