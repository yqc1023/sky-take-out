package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface DishMapper {

    /**
     * 根据分类id查询菜品数量
     * @param categoryId
     * @return
     */
    @Select("select count(id) from dish where category_id = #{categoryId}")
    Integer countByCategoryId(Long categoryId);


    /**
     * 新增菜品
     * @param dish
     */

    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    /**
     * 菜品分类查询
     * @param dishPageQueryDTO
     * @return
     */

    Page<DishVO> pageQuary(DishPageQueryDTO dishPageQueryDTO);

    /**
     * 批量删除菜品
     * @param id
     */
    void delete(Integer id);

    /**
     * 根据id查询菜品
     *
     * @param ids
     * @return
     */
    Integer selectById(Integer id);
}
