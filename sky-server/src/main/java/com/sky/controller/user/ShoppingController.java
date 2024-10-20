package com.sky.controller.user;


import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.ShoppingCart;
import com.sky.result.Result;
import com.sky.service.ShoppingService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C端购物车接口
 */
@RestController
@Slf4j
@RequestMapping("/user/shoppingCart")
public class ShoppingController {

    @Autowired
    private ShoppingService shoppingService;

    /**
     * 添加购物车
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/add")
    public Result addShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("添加购物车:{}", shoppingCartDTO);
        shoppingService.addShoppingCart(shoppingCartDTO);
        return Result.success();

    }

    /**
     * 查看购物车
     * @return
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> getShoppingCart() {
        log.info("查看购物车");
        List<ShoppingCart> list = shoppingService.getShoppingCart();
        return Result.success(list);
    }


    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public Result cleanShoppingCart() {
        log.info("清空购物车");
        shoppingService.cleanShoppingCart();
        return Result.success();
    }

    /**
     * 删除购物车的一个商品
     * @param shoppingCartDTO
     * @return
     */
    @PostMapping("/sub")
    public Result deleteShoppingCart(@RequestBody ShoppingCartDTO shoppingCartDTO) {
        log.info("删除购物车的一个商品");
        shoppingService.deleteShoppingCart(shoppingCartDTO);
        return Result.success();
    }
}
