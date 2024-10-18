package com.sky.service.impl;

import com.sky.mapper.ShopMapper;
import com.sky.service.ShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopMapper shopMapper;

    /**
     * 设置营业状态
     * @param status
     */
    @Override
    public void updateShopStatus(Integer status) {
        shopMapper.update(status);
    }

    /**
     * 获取营业状态
     * @return
     */
    @Override
    public Integer getShopStatus() {
        Integer status = shopMapper.select();
        return status;
    }
}
