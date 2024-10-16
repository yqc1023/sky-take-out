package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapperr {


    /**
     * 批量插入口味数据
     * @param flavors
     */
    void insert(List<DishFlavor> flavors);

    /**
     * 批量删除口味
     * @param dishIds
     */
    void deleteByIds(List<Long> dishIds);

    /**
     * 修改菜品口味
     *
     * @param
     * @param dishId
     */
    void updateWithFlavor(List<DishFlavor> flavors, Long dishId);

    /**
     * 单独删除菜品的口味数据
     * @param id
     */
    @Delete("delete from dish_flavor where dish_id = #{id}")
    void deleteById(Long id);
}
