package com.set.springbook.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserTable extends AbstractPersistable<Long> {
    private String name;
    @ManyToMany
    @JoinTable(name = "user_followers"
            , joinColumns = @JoinColumn(name = "user_id")
            , inverseJoinColumns = @JoinColumn(name = "follower_id"))
    private List<UserTable> followers;

    @ManyToMany(mappedBy = "followers")
    private List<UserTable> following;

}
