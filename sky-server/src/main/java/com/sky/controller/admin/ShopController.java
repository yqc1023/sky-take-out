package com.sky.controller.admin;


import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

/**
 * 管理端设置营业状态
 */
@Slf4j
@RestController("adminShopController")
@RequestMapping("/admin/shop")
public class ShopController {
    //为了一个status的值开一张表不值得
    //所以用Redis完成存储和获取
    public static final String KEY = "SHOP STATUS" ;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     *设置营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    public Result updateShopStatus(@PathVariable Integer status){
        log.info("设置营业状态:{}",status == 1 ? "营业中" : "打样中");
        redisTemplate.opsForValue().set(KEY,status);
        return Result.success();
    }

    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    public Result<Integer> getShopStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get(KEY);
        log.info("获取营业状态{}",status == 1 ? "营业中" : "打样中");
        return Result.success(status);
    }

}
