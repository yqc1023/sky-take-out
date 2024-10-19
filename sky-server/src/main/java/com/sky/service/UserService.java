package com.sky.service;


import com.sky.dto.UserLoginDTO;
import com.sky.vo.UserLoginVO;

public interface UserService {

    /**
     *
     * @param userLoginDTO
     * @return
     */
    UserLoginVO login(UserLoginDTO userLoginDTO);

}
