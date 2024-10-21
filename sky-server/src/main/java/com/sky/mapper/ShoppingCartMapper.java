package com.sky.mapper;

import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface ShoppingCartMapper {



    /**
     * 根据套餐id和菜品id查询当前购物车中的数据
     * @param shoppingCart
     */
    List<ShoppingCart> list (ShoppingCart shoppingCart);

    /**
     *根据id修改商品数量
     * @param cart
     */
    void updateNumberById(ShoppingCart cart);

    /**
     *新增购物车
     * @param shoppingCart
     */
    @Insert("insert into shopping_cart (name, image, user_id, dish_id, setmeal_id, dish_flavor, amount, create_time) " +
            "VALUES " +
            "(#{name},#{name},#{userId},#{dishId},#{setmealId},#{dishFlavor},#{amount},#{createTime})")
    void insert(ShoppingCart shoppingCart);

    /**
     * 删除购物车的一个商品
     * @param Cart
     */
    void delete(ShoppingCart Cart);



    /**
     * 清空购物车
     * @param userId
     */
    @Delete("delete from shopping_cart where user_id = #{userId}")
    void cleanAllByUserId(Long userId);


    /**
     * 根据userId查询购物车
     * @param userId
     * @return
     */
    @Select("select * from shopping_cart where user_id = #{userId}")
    List<ShoppingCart> selectByUserId(Long userId);
}
