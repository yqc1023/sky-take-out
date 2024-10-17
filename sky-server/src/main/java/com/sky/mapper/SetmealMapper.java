package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper
public interface SetmealMapper {


    /**
     * 根据分类id查询套餐的数量
     * @param id
     * @return
     */
    @Select("select count(id) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 套餐的分类查询
     * @param setmealPageQueryDTO
     * @return
     */
    Page<Setmeal> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Select("select * from setmeal where id = #{id}")
    SetmealVO getById(Integer id);

    /**
     * 新增套餐
     * @param setmeal
     */
    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 根据id设置套餐的起售与停售
     * @param status
     * @param id
     */
    @Update("update setmeal set status = #{status} where id = #{id}")
    void updateStatus(Integer status, Integer id);

    /**
     * 根据ids批量查询套餐信息
     * @param ids
     * @return
     */
    List<Setmeal> getByIds(List<Integer> ids);

    /**
     * 根据ids批量删除套餐
     * @param ids
     */
    void delete(List<Integer> ids);

    /**
     * 修改套餐
     * @param setmealVO
     */
    @AutoFill(value = OperationType.UPDATE)
    void update(SetmealVO setmealVO);
}
