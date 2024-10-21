package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ShoppingServiceImpl implements ShoppingService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealMapper setmealMapper;
    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     */
    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {
        //判断当前加入购物车中的商品是否已经存在了
        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);

        //从线程中获取当前用户的id
        Long userId =BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = shoppingCartMapper.list(shoppingCart);


        //如果已经存在了,只需将数量+1
        if (list != null && list.size() > 0) {
            ShoppingCart cart = list.get(0);
            Integer number = cart.getNumber();
            cart.setNumber(++number);

            //再从数据库中修改数量
            shoppingCartMapper.updateNumberById(cart);
        }
        else {
            //如果不存在,需要插入一条购物车数据

            //判断本次添加到购物车的是菜品还是套餐
            Long dishId = shoppingCartDTO.getDishId();
            Long setmealId = shoppingCartDTO.getSetmealId();

            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);

            if (dishId != null) {//如果菜品id不为空,则本次添加进购物车的是菜品
                Dish dish = dishMapper.selectById(dishId);
                shoppingCart.setNumber(1);
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setAmount(dish.getPrice());
                shoppingCart.setName(dish.getName());

                //新增购物车
                shoppingCartMapper.insert(shoppingCart);
            }
            else  {//如果套餐id不为空,则本次添加进购物车的是套餐
                Setmeal setmeal = setmealMapper.getById(setmealId);

                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setAmount(setmeal.getPrice());
                shoppingCart.setName(setmeal.getName());

                //新增购物车
                shoppingCartMapper.insert(shoppingCart);

            }
        }
    }

    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<ShoppingCart> getShoppingCart() {
        Long userId = BaseContext.getCurrentId();
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUserId(userId);
        List<ShoppingCart> list = setmealMapper.selectByUserId(shoppingCart);
        return list;
    }

    /**
     * 清空购物车
     */
    @Override
    public void cleanShoppingCart() {
        shoppingCartMapper.cleanAllByUserId(BaseContext.getCurrentId());
    }

    /**
     * 删除购物车的一个商品
     * @param shoppingCartDTO
     */
    @Override
    public void deleteShoppingCart(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCart shoppingCart = new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO, shoppingCart);
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);

        //查询用户购物车中被删除的商品的数量
        ShoppingCart cart = setmealMapper.selectDishByUserId(shoppingCart);
        //判断数量是否大于1
        if (cart.getNumber() == 1) {
            shoppingCartMapper.delete(shoppingCart);
        }
        else if (cart.getNumber() > 1) {
            shoppingCart.setNumber(cart.getNumber() - 1);
            shoppingCartMapper.updateNumberById(shoppingCart);
        }
    }
}
