package com.sky.mapper;


import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapperr {


    /**
     * 批量插入口味数据
     * @param flavors
     */
    void insert(List<DishFlavor> flavors);

    /**
     * 批量删除口味
     * @param dishIds
     */
    void delete(List<Long> dishIds);
}
