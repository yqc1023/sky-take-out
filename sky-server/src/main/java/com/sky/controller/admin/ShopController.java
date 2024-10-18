package com.sky.controller.admin;


import com.sky.result.Result;
import com.sky.service.ShopService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin/shop")
public class ShopController {
    @Autowired
    private ShopService shopService;

    /**
     *设置营业状态
     * @param status
     * @return
     */
    @PutMapping("/{status}")
    public Result updateShopStatus(@PathVariable Integer status){
        log.info("设置营业状态:{}",status);
        shopService.updateShopStatus(status);
        return Result.success();
    }

    /**
     * 获取营业状态
     * @return
     */
    @GetMapping("/status")
    public Result<Integer> getShopStatus(){
        log.info("获取营业状态");
        Integer status = shopService.getShopStatus();
        return Result.success(status);
    }

}
