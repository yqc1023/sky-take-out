package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealDishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetmealService;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetmealServiceImpl implements SetmealService {
    @Autowired
     private SetmealMapper setmealMapper;

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private SetmealDishMapper setmealDishMapper;
    /**
     * 分类查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());
        Page<Setmeal> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }




    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Transactional
    @Override
    public void insert(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

        //将套餐信息插入
        setmealMapper.insert(setmeal);

        //将套餐包含的菜品信息插入
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmeal.getId());

        }
        setmealDishMapper.insert(setmealDishes);
    }

    /**
     * 套餐的起售和停售
     * @param status
     * @param id
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        //获取套餐中包含的菜品信息
        List<SetmealDish> setmealDishes = setmealDishMapper.getDishIdsBySetmealId(id);
        List<Long> dishIds = new ArrayList<>();
        for (SetmealDish setmealDish : setmealDishes) {
            dishIds.add(setmealDish.getDishId());
        }
        List<Dish> dishes = dishMapper.getByDishIds(dishIds);

        //判断套餐中是否包含停售的菜品
        for (Dish dish : dishes) {
            if (dish.getStatus() == StatusConstant.DISABLE){//如果有则不能起售
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
            }
        }
        //如果没有则起售
        setmealMapper.updateStatus(status,id);
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    public void delete(List<Integer> ids) {
        //通过id获取被删除的套餐信息
        List<Setmeal> setmeals = setmealMapper.getByIds(ids);

        //判断被删除的套餐中是否有起售的套餐
        for (Setmeal setmeal : setmeals) {
            if (setmeal.getStatus() == StatusConstant.ENABLE){//如果有,则不能删除
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        }
        setmealMapper.delete(ids);
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmealDTO, setmealVO);
        //将新的套餐信息修改进表中
        setmealMapper.update(setmealVO);

        //将套餐中原有的菜品删除
        setmealDishMapper.deleteBySetmealId(setmealDTO.getId());

        //将新的套餐中的菜品插入套餐-菜品表中
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        for (SetmealDish setmealDish : setmealDishes) {
            setmealDish.setSetmealId(setmealDTO.getId());
        }
        setmealDishMapper.insert(setmealDishes);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    public List<Setmeal> list(Setmeal setmeal) {
        List<Setmeal> list = setmealMapper.list(setmeal);
        return list;
    }

    /**
     * 根据id查询菜品选项
     * @param id
     * @return
     */
    public List<DishItemVO> getDishItemById(Long id) {
        return setmealMapper.getDishItemBySetmealId(id);
    }


    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getById(Long id) {
        Setmeal setmeal = setmealMapper.getById(id);
        SetmealVO setmealVO = new SetmealVO();
        BeanUtils.copyProperties(setmeal, setmealVO);
        List<SetmealDish> dishIdsBySetmeal = setmealDishMapper.getDishIdsBySetmealId(setmeal.getId());
        setmealVO.setSetmealDishes(dishIdsBySetmeal);
        return setmealVO;
    }
}
