package com.sky.service;

public interface ShopService {
    /**
     * 设置营业状态
     * @param status
     */
    void updateShopStatus(Integer status);

    /**
     * 获取营业状态
     * @return
     */
    Integer getShopStatus();

}
