package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapperr;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.utils.AliOssUtil;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class DishServiceimpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private DishFlavorMapperr dishFlavorMapperr;

    @Autowired
    private SetmealDishMapper setmealDishMapperr;
    /**
     * 新增菜品
     * @param dishDTO
     */
    @Transactional//事务管理,回滚操作,要么一起成功要么一起失败(因为此方法操控两个数据库)
    @Override
    public void saveWithFlavor(DishDTO dishDTO) {
        //向菜品表插入数据
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO, dish);
        //获取inert语句中的主键(id)值
        //具体可看xml文件
        // useGeneratedKeys="true" 代表返回本条操作数据的主键值
        // keyProperty="id"  表示将返回的主键值赋值给mapper的形参(Dish dish)的对象中
        // separator=","  表示每次循环后添加一个',' insert into dish_flavor () VALUES (第一组循环数据) , (第二组循环数据)  中间添加','
        dishMapper.insert(dish);
        //此时形参dish已经活的到刚刚操作的数据的id值


        //获取口味数据
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors.size() > 0 && flavors!=null) {//判断口味是否为空(有没有添加口味的可能)
            //像口味表插入n条数据
            //由于口味数据需要匹配(里面有此口味对应dishid,而此id前端无法传回)
            //所以只能从上面刚刚插入的dish数据中返回被插入的数据的id,并将其赋值到形参(dish对象)上
            //现在要把dish对象中的返回的dishid赋值到flavors中
            flavors.forEach(flavor -> flavor.setDishId(dish.getId()));
            dishFlavorMapperr.insert(flavors);
        }


    }

    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuary(DishPageQueryDTO dishPageQueryDTO) {
        PageHelper.startPage(dishPageQueryDTO.getPage(),dishPageQueryDTO.getPageSize());
        Page<DishVO> dishVOS=dishMapper.pageQuary(dishPageQueryDTO);
        return new PageResult(dishVOS.getTotal(),dishVOS.getResult());
    }


    /**
     * 批量删除口味
     * @param ids
     */
    @Transactional
    @Override
    public void delete(List<Integer> ids) {//不对
                                            //status为0不能删
        for (Integer id : ids) {
            Integer status= dishMapper.selectById(id);
            if (status == 1){
                dishMapper.delete(id);
                dishFlavorMapperr.delete(id);
                setmealDishMapperr.delete(id);
            }
        }


        if (!dishs.isEmpty() ) {
            dishMapper.delete(ids);
            dishFlavorMapperr.delete(ids);
            setmealDishMapperr.delete(ids);
        }

    }
}
