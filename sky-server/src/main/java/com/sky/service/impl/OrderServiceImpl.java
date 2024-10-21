package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.OrdersPageQueryDTO;
import com.sky.dto.OrdersSubmitDTO;
import com.sky.entity.AddressBook;
import com.sky.entity.OrderDetail;
import com.sky.entity.Orders;
import com.sky.entity.ShoppingCart;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.AddressBookMapper;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.result.PageResult;
import com.sky.service.OrderService;
import com.sky.vo.OrderSubmitVO;
import com.sky.vo.OrderVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;

    @Autowired
    private OrderDetailMapper orderDetailMapper;

    @Autowired
    private AddressBookMapper addressBookMapper;

    /**
     * 用户下单
     * @param ordersSubmitDTO
     * @return
     */
    @Transactional
    @Override
    public OrderSubmitVO submitOrder(OrdersSubmitDTO ordersSubmitDTO) {
        //处理各种异常(地址簿为空、购物车为空)
        AddressBook address = addressBookMapper.getById(ordersSubmitDTO.getAddressBookId());
        List<ShoppingCart> shoppingCarts = shoppingCartMapper.selectByUserId(BaseContext.getCurrentId());

         if(address == null){
            throw new DeletionNotAllowedException(MessageConstant.ADDRESS_BOOK_IS_NULL);
         } else if (shoppingCarts.size() <= 0) {
             throw new DeletionNotAllowedException(MessageConstant.SHOPPING_CART_IS_NULL);
         }


        //向订单表插入1条数据
        Orders orders = new Orders();
        BeanUtils.copyProperties(ordersSubmitDTO,orders);
        //设置用户id
        orders.setUserId(BaseContext.getCurrentId());
        //设置下单时间
        orders.setOrderTime(LocalDateTime.now());
        //设置支付状态 未支付
        orders.setPayStatus(Orders.UN_PAID);
        //设置订单状态为 待付款
        orders.setStatus(Orders.PENDING_PAYMENT);
        //添加订单号 使用当前系统时间戳
        orders.setNumber(String.valueOf(System.currentTimeMillis()));
        //添加手机号
        orders.setPhone(address.getPhone());
        //设置收货人
        orders.setConsignee(address.getConsignee());
        //添加订单信息
        //这里要将id返回,因为下面添加订单明细表的时候需要添加当前的订单id
        orderMapper.insert(orders);


        //向订单明细表插入n条调数据
        List<OrderDetail> orderDetailList = new ArrayList<>();
        for (ShoppingCart cart : shoppingCarts) {
            OrderDetail orderDetail = new OrderDetail();
            BeanUtils.copyProperties(cart,orderDetail);
            //添加本次订单明细关联的订单id
            orderDetail.setOrderId(orders.getId());
            orderDetailList.add(orderDetail);
        }
        //批量插入数据
        orderDetailMapper.insertBatch(orderDetailList);

        //下单成功后,清空购物车数据
        shoppingCartMapper.cleanAllByUserId(BaseContext.getCurrentId());

        OrderSubmitVO orderSubmitVO = OrderSubmitVO.builder()
                .id(orders.getId())
                .orderAmount(orders.getAmount())
                .orderTime(orders.getOrderTime())
                .orderNumber(orders.getNumber()).build();
        return orderSubmitVO;
    }

    /**
     * 历史订单查询
     * @param ordersPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(OrdersPageQueryDTO ordersPageQueryDTO) {
        //分页
        PageHelper.startPage(ordersPageQueryDTO.getPage(),ordersPageQueryDTO.getPageSize());

        //查询所有订单基本信息
        Long userId = BaseContext.getCurrentId();
        Orders orders = new Orders();
        orders.setUserId(userId);
        orders.setStatus(ordersPageQueryDTO.getStatus());
        //因为分页查询主要依据订单基本信息,所以设置一页查多少和一共几页由查询订单基本信息决定
        //因此在查询订单基本信息时完成分页
        Page<Orders> page =  orderMapper.pageQury(orders);

        List<OrderVO> ordersVOS = new ArrayList<>();
        //遍历所有订单基本信息,每个订单对应着一个详细信息,根据订单id进行查找,一一匹配
        //最后将匹配好的数据封装为ordersDTO存入数组中
        for (Orders order : page) {
            OrderVO ordersVO = new OrderVO();
            List<OrderDetail> orderDetails = orderDetailMapper.selectByOrderId(order.getId());
            BeanUtils.copyProperties(order,ordersVO);
            ordersVO.setOrderDetailList(orderDetails);
            ordersVOS.add(ordersVO);
        }
        //这里的数据部分就是查询的全部结果
        return new PageResult(page.getTotal(),ordersVOS);
    }

    /**
     * 再来一单
     * @param id
     */
    @Override
    public void oneMoreOrder(Integer id) {
        //查询需要再来一单的样单信息
        Orders orders = new Orders();
        orders.setId(Long.valueOf(id));
        Orders ordersCopy = orderMapper.select(orders);
        List<OrderDetail> orderDetailListCopy = orderDetailMapper.selectByOrderId(ordersCopy.getId());



    }
}
