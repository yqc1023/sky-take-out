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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @PostMapping                           //这里的key取值必须是与形参一致
    //@CachePut(cacheNames = "userCache" , key = "#dishDTO.id")  //使用spring Cache缓存数据,key的生成: userCache::xxxx(1或2或3)
    @CachePut(cacheNames = "userCache" , key = "#p0.id")//获取方法的第一个形参  清理缓存\

    public Result save (@RequestBody DishDTO dishDTO){
        log.info("新增菜品:{}",dishDTO);
        dishService.saveWithFlavor(dishDTO);
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
    @CacheEvict(cacheNames = "userCache" , allEntries = true) // allEntries :userCache所有的键值对都会被删除
    public Result delete(@RequestParam List<Long> ids){
        log.info("菜品批量删除:{}",ids);
        dishService.deleteBatch(ids);

        //清理缓存数据
        //删除多个缓存的情况则将其全部删除(先查出来再删除)
        //cleanCache("dish_*");

        return Result.success();
    }

    /**
     * 根据id查询菜品信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @Cacheable(cacheNames = "userCache" , key = "#p0")
    //spring Cache的底层是基于代理技术,加入注解后spring Cache会为当前Controller类创建一个代理对象,并先进入代理对象,在里面查询redis
    //当代理对象在redis查到对应的缓存数据时,会直接将数据返回,不会运行本方法
    //当代理对象在redis查不到对应的缓存数据时,CachePut底层会使用反射获取方法的返回值(dishVO),将其存入redis
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
    @CacheEvict(cacheNames = "userCache" , allEntries = true)
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status , Long id){
        log.info("修改菜品状态:{},{}",status,id);
        dishService.updateStatus(status,id);
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
