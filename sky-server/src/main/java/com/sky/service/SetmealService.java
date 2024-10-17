package com.sky.service;


import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;
import org.springframework.stereotype.Service;

@Service
public interface SetmealService {


    /**
     * 套餐管理
     * @param setmealService
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO setmealService);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealVO getById(Integer id);

    /**
     * 新增套餐
     * @param setmealDTO
     */
    void insert(SetmealDTO setmealDTO);


}
