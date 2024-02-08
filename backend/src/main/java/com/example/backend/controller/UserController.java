package com.example.backend.controller;

import com.example.backend.common.BaseResponse;
import com.example.backend.common.ErrorCode;
import com.example.backend.common.ResultUtils;
import com.example.backend.exception.BusinessException;
import com.example.backend.model.dto.user.UserLoginRequest;
import com.example.backend.model.dto.user.UserRegisterRequest;
import com.example.backend.model.vo.UserVO;
import com.example.backend.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("user")
public class UserController {
    @Resource
    private UserService userService;
    @PostMapping("register")
    public BaseResponse<Boolean> userRegister(@RequestBody UserRegisterRequest userRegisterRequest){
        if(userRegisterRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Boolean result = userService.userRegister(userRegisterRequest.getUserAccount(),
                userRegisterRequest.getPassword(),
                userRegisterRequest.getCheckPassword());
        return ResultUtils.success(result);
    }
    @PostMapping("login")
    public BaseResponse<UserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request){
        if(userLoginRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserVO userVO = userService.userLogin(userLoginRequest.getUserAccount(),
                userLoginRequest.getPassword(), request);
        return ResultUtils.success(userVO);
    }
    @PostMapping("logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request){
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }
    @GetMapping("getLoginUser")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request){
        UserVO userVO = userService.getLoginUser(request);
        return ResultUtils.success(userVO);
    }

}
