package com.sky.mapper;


import com.sky.annotation.AutoFill;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 插入套餐中包含的菜品信息
     * @param setmealDish
     */

     void insert(SetmealDish setmealDish);

    /**
     * 根据菜品id查询套餐id
     * @param dishIds
     * @return
     */
    List<Long> getSetmealIdsByDishIds(List<Long> dishIds);

    /**
     * 根据setmealId查询菜品信息
     * @param setmealId
     * @return
     */
    @Select("select * from  setmeal_dish where setmeal_id = #{setmealId}")
    List<SetmealDish> getDishIdsBySetmealId(Integer setmealId);
}
