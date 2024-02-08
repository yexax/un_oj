package com.example.backend.model.vo;

import lombok.Data;

import java.util.Date;
@Data
public class UserVO {
    private Long id;
    private String userAccount;
    private String username;
    private String userRole;
    private Date createTime;
    private Date updateTime;
}
