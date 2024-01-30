package com.set.springbook.model;


import lombok.Data;


import java.util.List;

@Data
public class AccountDto {
    private Long id;
    private String username;
    private List<Long> followers;
    private List<Long> following;
}
