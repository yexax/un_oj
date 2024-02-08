package com.example.backend.service;

import com.example.backend.model.entities.User;
import com.example.backend.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
    Boolean userRegister(String userAccount,String password,String checkPassword);
    UserVO userLogin(String userAccount, String password, HttpServletRequest request);
    UserVO getLoginUser(HttpServletRequest request);
    boolean isAdmin(HttpServletRequest request);
    boolean userLogout(HttpServletRequest request);

    /**
     * 把user脱敏
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

}
