package com.sky.mapper;


import com.github.pagehelper.Page;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OrderMapper {

    /**
     * 添加订单信息
     * @param orders
     */

    void insert(Orders orders);

    /**
     * 查询所有订单
     * @param orders
     * @return
     */
    Page<Orders> pageQury(Orders orders);

    /**
     * 查询订单基本信息
     * @param orders
     * @return
     */
    @Select("select * from  orders where id = #{id}")
    Orders select(Orders orders);
}
