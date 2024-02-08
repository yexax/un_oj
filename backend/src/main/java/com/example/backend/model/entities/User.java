package com.example.backend.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String userAccount;
    private String password;
    private String username;
    private String userRole;
    private Date createTime;
    private Date updateTime;
    private Integer isDelete;
}
