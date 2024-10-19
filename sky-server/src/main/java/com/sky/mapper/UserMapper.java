package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.User;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    /**
     * 根据openId查询是否存在此用户
     * @param openId
     * @return
     */
    @Select("select * from user where openid = #{openId}")
    User selectByOpenId(String openId);

    /**
     * 添加用户
     * @param user
     */
    void insert(User user);
}
