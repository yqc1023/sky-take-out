package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {


    @Autowired
    private EmployeeMapper employeeMapper;

   /* @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private HttpServletRequest request;*/
    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 对前端传过来的明文密码进行加密处理
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }


        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //3、返回实体对象
        return employee;
    }


    /**
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {

        Employee employee= new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);

        //设置默认密码123456,并用md5加密
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //设置创建时间和修改时间
        // 这一步在AOP中操作了(见com.sky.aspect.AutoFillAspect)

        //设置账号状态
        employee.setStatus(StatusConstant.ENABLE);

        //设置创建人id和修改人id
        //用jwt获取
        //String jwt = request.getHeader(jwtProperties.getAdminTokenName());
        //Claims claims = JwtUtil.parseJWT(jwtProperties.getAdminSecretKey(), jwt);
        //TODO




        //通过ThreadLocal这个线程的存储空间来储存数据,并使用get(),remove()方法获取和删除数据
        //为了方便使用,直接将ThreadLocal的相关方法和创建其对象的代码封装入BaseContext类中

//        employee.setUpdateUser(BaseContext.getCurrentId());
//        employee.setCreateUser(BaseContext.getCurrentId());

        //设置创建/修改时间和创建/修改人
        // 这一步在AOP中操作了(见com.sky.aspect.AutoFillAspect)


        employeeMapper.insert(employee);

    }

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(),employeePageQueryDTO.getPageSize());
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        return new PageResult(page.getTotal(),page.getResult());

    }

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Employee  employee = Employee.builder()//创建构建器来创建对象并赋值,更高级点,
                                .status(status)// 但被创建的对象的类要有@Builder注解
                                .id(id).build();//设置创建/修改时间和创建/修改人
                                                 // 这一步在AOP中操作了(见com.sky.aspect.AutoFillAspect)
        employeeMapper.update(employee);
    }


    /**
     * 编辑员工信息
     * @param employeeDTO
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO,employee);
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser(BaseContext.getCurrentId());
        //设置创建/修改时间和创建/修改人
        // 这一步在AOP中操作了(见com.sky.aspect.AutoFillAspect)
        employeeMapper.update(employee);
    }


    /**
     * 根据id查找员工
     * @param id
     * @return
     */
    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.getById(id);
        return employee;
    }


}
