package com.sky.controller.user;


import com.sky.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户端设置营业状态
 */
@Slf4j
@RestController
@RequestMapping("/user/shop")
public class ShopController {
    //为了一个status的值开一张表不值得
    //所以用Redis完成存储和获取

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    public Result<Integer> getShopStatus(){
        Integer status = (Integer) redisTemplate.opsForValue().get("SHOP STATUS");
        log.info("获取营业状态{}",status == 1 ? "营业中" : "打样中");
        return Result.success(status);
    }
}
