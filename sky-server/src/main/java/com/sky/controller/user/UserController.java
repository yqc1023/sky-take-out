package com.sky.controller.user;


import com.sky.dto.UserLoginDTO;
import com.sky.result.Result;
import com.sky.service.UserService;
import com.sky.vo.UserLoginVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequestMapping("/user/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 微信登录
     * @param userLoginDTO
     * @return
     */
    @PostMapping("/login")
    private Result<UserLoginVO> login(@RequestBody  UserLoginDTO userLoginDTO){
        log.info("微信登录:{}",userLoginDTO.getCode());
        UserLoginVO userLoginVO = userService.login(userLoginDTO);
        return Result.success(userLoginVO);
    }

}
