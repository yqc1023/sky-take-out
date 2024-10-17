package com.sky.mapper;


import com.sky.entity.SetmealDish;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface SetmealDishMapper {

    /**
     * 插入套餐中包含的菜品信息
     * @param setmealDishes
     */

     void insert(List<SetmealDish> setmealDishes);

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

    /**
     * 根据setmealId批量删除套餐中的菜品信息
     * @param setmealId
     */
    @Delete("delete from setmeal_dish where setmeal_id = #{setmealId}")
    void deleteBySetmealId(Long setmealId);
}
