package com.sky.mapper;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ShopMapper {

    /**
     * 设置营业状态
     * @param status
     */

    void update(Integer status);

    /**
     * 获取营业状态
     * @return
     */
    Integer select();

}
