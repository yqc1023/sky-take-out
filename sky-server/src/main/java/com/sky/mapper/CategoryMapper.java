package com.sky.mapper;

import com.sky.entity.Category;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper {

    /**
     * 新增分类
     * @param category
     */
    @Insert("insert into category ( type, name, sort,  create_time, update_time, create_user, update_user,status) " +
            "VALUES" +
            " (#{type},#{name},#{sort},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
    void Insert(Category category);
}
