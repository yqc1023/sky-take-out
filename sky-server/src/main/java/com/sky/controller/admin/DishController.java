package com.sky.controller.admin;


import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 菜品管理
 */
@Slf4j
@RestController
@RequestMapping("/admin/dish")
public class DishController {
    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private DishService dishService;

    /**
     * 新增菜品
     * @param dishDTO
     * @return
     */
    @PostMapping
    public Result save (@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
        //清理缓存数据
        String key = "dish_"+dishDTO.getCategoryId();
        cleanCache(key);
        return Result.success();
    }


    /**
     * 菜品分页查询
     * @param dishPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(DishPageQueryDTO dishPageQueryDTO){
        log.info("菜品分页查询:{}",dishPageQueryDTO);
        PageResult pageResult = dishService.pageQuary(dishPageQueryDTO);
        return Result.success(pageResult);
    }


    /**
     * 菜品批量删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品批量删除:{}",ids);
        dishService.deleteBatch(ids);

        //清理缓存数据
        //删除多个缓存的情况则将其全部删除(先查出来再删除)
        cleanCache("dish_*");

        return Result.success();
    }

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id){
        log.info("根据id查询菜品信息:{}",id);
        DishVO  dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }


    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){
        log.info("修改菜品:{},{}",dishDTO,dishDTO.getId());
        dishService.updateWithFlavor(dishDTO);


        //清理缓存数据
        //需要删除多个缓存的情况则将其全部删除(先查出来再删除)
        cleanCache("dish_*");
        return Result.success();
    }

    /**
     * 修改菜品状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status , Long id){
        log.info("修改菜品状态:{},{}",status,id);
        dishService.updateStatus(status,id);
        //清理缓存数据
        //需要删除多个缓存的情况则将其全部删除(先查出来再删除)
        cleanCache("dish_*");
        return Result.success();

    }

    /**
     *根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Dish>> getByCategoryId( Long categoryId ){
        log.info("根据分类id查询菜品");
        List<Dish> dishes = dishService.getByCategoryId(categoryId);
        return Result.success(dishes);
    }

    /**
     *清理缓存
     * @param pattern
     */
    private void cleanCache(String pattern){
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }
}
