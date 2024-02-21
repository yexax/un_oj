package com.example.backend.model.dto.user;

import lombok.Data;

import java.io.Serializable;
@Data
public class UserRegisterRequest implements Serializable {
    private String userAccount;
    private String password;
    private String checkPassword;
}
