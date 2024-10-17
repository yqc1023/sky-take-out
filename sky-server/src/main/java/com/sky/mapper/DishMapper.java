package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.entity.SetmealDish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
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
     * @param ids
     */
    void delete(List<Long> ids);

    /**
     * 根据主键id查询菜品信息
     *
     * @param id
     * @return
     */
    @Select("select * from  dish where  id = #{id}")
    Dish selectById(Long id);



    /**
     * 根据菜品id查询口味数据
     * @param dishId
     * @return
     */
    @Select("select * from dish_flavor where dish_id = #{dishId}")
    List<DishFlavor> getDishFlavorByDishId(Long dishId);

    /**
     * 修改菜品及口味
     * @param dish
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @Select("select * from dish where category_id = #{categoryId}")
    List<Dish> getByCategoryId(Long categoryId);

    /**
     * 根据dishId查询菜品信息
     * @param dishIds
     * @return
     */
    List<Dish> getByDishIds(List<Long> dishIds);
}
