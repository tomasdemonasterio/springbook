package com.set.springbook.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @JsonIgnore
    private String password;

    @JsonIgnore
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> authorities;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(name = "user_followers"
            , joinColumns = @JoinColumn(name = "user_id")
            , inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private List<Account> followers;

    @JsonBackReference
    @ManyToMany(mappedBy = "followers")
    private List<Account> following;
}
