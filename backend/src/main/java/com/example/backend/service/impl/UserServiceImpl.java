package com.example.backend.service.impl;

import com.example.backend.common.ErrorCode;
import com.example.backend.constant.UserConstant;
import com.example.backend.exception.BusinessException;
import com.example.backend.mapper.UserMapper;
import com.example.backend.model.entities.User;
import com.example.backend.model.vo.UserVO;
import com.example.backend.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.example.backend.constant.UserConstant.USER_LOGIN_STATE;
@Service
public class UserServiceImpl implements UserService {
    public static final String SALT="Bearer ";
    @Resource
    private UserMapper userMapper;
    @Override
    public Boolean userRegister(String userAccount, String password, String checkPassword) {
        //参数校验
        if(StringUtils.isAnyBlank(userAccount,password,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号过短");
        }
        if(password.length()<6){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码过短");
        }
        if(!password.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入密码不一致");
        }
        if (userMapper.getUserByUserAccount(userAccount)!=null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号已存在");
        }
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        //插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setPassword(encryptPassword);
        boolean saveResult = userMapper.insertUser(user);
        if(!saveResult){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"注册失败，数据库错误");
        }
        return true;
    }

    @Override
    public UserVO userLogin(String userAccount, String password, HttpServletRequest request) {
        //参数校验
        if(StringUtils.isAnyBlank(userAccount,password)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数为空");
        }
        if(userAccount.length()<4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号错误");
        }
        if(password.length()<8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
        }
        //密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        User user = userMapper.getUserByUserAccount(userAccount);
        if(user==null||!encryptPassword.equals(user.getPassword())){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号或密码错误");
        }
        UserVO userVO = getUserVO(user);
        request.getSession().setAttribute(USER_LOGIN_STATE,userVO);
        return userVO;
    }

    @Override
    public UserVO getLoginUser(HttpServletRequest request) {
        UserVO currentUser = (UserVO) request.getSession().getAttribute(USER_LOGIN_STATE);
        if(currentUser==null||currentUser.getId()==null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        UserVO loginUser = getLoginUser(request);
        return loginUser.getUserAccount().equals(UserConstant.ADMIN_ROLE);
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        getLoginUser(request);
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public UserVO getUserVO(User user) {
        if(user==null){
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return userVO;
    }
}
