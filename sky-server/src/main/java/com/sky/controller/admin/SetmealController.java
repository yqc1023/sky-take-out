package com.sky.controller.admin;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.SetmealDish;
import com.sky.mapper.SetmealDishMapper;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import com.sky.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 套餐管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {
    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishMapper setmealDishMapper;

    /**
     * 套餐的分类查询
     * @param setmealPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(SetmealPageQueryDTO setmealPageQueryDTO) {
        log.info("套餐的分类管理:{}", setmealPageQueryDTO);
        PageResult pageResult = setmealService.pageQuery(setmealPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealVO> getById(@PathVariable Integer id) {
        log.info("根据id查询套餐:{}",id);
        SetmealVO setmealVO = setmealService.getById(id);
        return Result.success(setmealVO);
    }

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @PostMapping
    public Result save(@RequestBody SetmealDTO setmealDTO) {
        log.info("新增套餐:{}",setmealDTO);

        //将套餐信息插入
        setmealService.insert(setmealDTO);

        //将套餐包含的菜品信息插入
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishMapper.insert(setmealDishes);
        return Result.success();
    }

}
