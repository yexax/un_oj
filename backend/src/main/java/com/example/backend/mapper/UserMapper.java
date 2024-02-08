package com.example.backend.mapper;

import com.example.backend.model.entities.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {
    User getUserById(Long id);
    User getUserByUserAccount(String Account);
    boolean insertUser(User user);
}
