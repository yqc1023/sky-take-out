package com.sky.mapper;


import com.sky.service.DishService;
import com.sky.service.impl.DishServiceimpl;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface SetmealDishMapper {


    /**
     * 批量删除菜品
     * @param id
     */
    void delete(Integer id);
}
