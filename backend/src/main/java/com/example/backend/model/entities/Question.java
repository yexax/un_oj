package com.example.backend.model.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    private Long id;
    private String title;
    private String content;
    private String tags;
    private Integer submitNum;
    private Integer acceptNum;
    private String judgeCase;
    private String judgeConfig;
    private Long userId;
    private Date createTime;
    private Date updateTime;
    private Integer isDelete;
}
