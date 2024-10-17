package com.sky.mapper;


import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 插入套餐中包含的菜品信息
     * @param setmealDishes
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into setmeal_dish (setmeal_id, dish_id, name, price, copies) " +
            "VALUES " +
            "(#{setmealId},#{dishId},#{name},#{price},#{price})")
     void insert(List<SetmealDish> setmealDishes);

    /**
     * 根据菜品id查询套餐id
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);
}
