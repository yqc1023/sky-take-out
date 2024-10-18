package com.sky.controller.user;


import com.sky.result.Result;
import com.sky.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/user/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    public Result<Integer> getShopStatus(){
        log.info("获取营业状态");
        Integer shopStatus = shopService.getShopStatus();
        return Result.success(shopStatus);
    }
}
