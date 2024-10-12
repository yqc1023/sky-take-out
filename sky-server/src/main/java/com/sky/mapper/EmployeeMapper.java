package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.aspectj.lang.annotation.Around;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 添加员工
     * @param employee
     * @return
     */
    @AutoFill(value = OperationType.INSERT)
    @Insert("insert into employee ( name, username, password, phone, sex, id_number, status, create_time, update_time, create_user, update_user)" +
            " VALUES" +
            " (#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{status},#{createTime},#{updateTime},#{createUser},#{updateUser})")
    void insert(Employee employee);

    /**
     * 添加员工
     * @param employeePageQueryDTO
     * @return
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 编辑员工信息
     * 启用禁用员工账号
     * @param employee
     */
    //此处因为两个功能都是对员工的修改操作(update)
    //所以复用xml文件,具体可看其xml文件中的sql代码
    @AutoFill(value = OperationType.UPDATE)
    void update(Employee employee);





    /**
     * 根据id查询员工
     * @param id
     * @return
     */
    @Select("select  * from  employee where  id = #{id}")
    Employee getById(Long id);
}
