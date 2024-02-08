package com.example.backend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserLoginRequest implements Serializable {
    private static final long serialVersionUID = 3191221716373120793L;
    private String userAccount;
    private String password;
}
