package com.zjj.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjj.reggie.common.R;
import com.zjj.reggie.mapper.EmployeeMapper;
import com.zjj.reggie.pojo.Employee;
import com.zjj.reggie.service.EmployeeService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService{
    @Resource
    private EmployeeMapper employeeMapper;
    @Override
    public R<Employee> checkLogin(Employee employee) {
        String password = DigestUtils.md5DigestAsHex(employee.getPassword().getBytes(StandardCharsets.UTF_8));
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee selectEmp = employeeMapper.selectOne(employeeLambdaQueryWrapper);
        String message = "";
        boolean isPass = false;
        if(selectEmp == null){
            message = "用户名不存在";
        }else if(!password.equals(selectEmp.getPassword())){
            message = "密码不正确";
        }else if(selectEmp.getStatus() == 0){
          message = "账号已被锁定";
        } else {
            isPass = true;
        }
        if(isPass)
            return R.success(selectEmp);
        else
            return R.error(message);
    }

    @Override
    public R<String> addNewEmployee(Employee employee){
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        save(employee);
        return R.success("添加成功");
    }

    @Override
    public IPage<Employee> getEmployeePage(Integer page, Integer pageSize, String name) {
        Page<Employee> employeePage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        employeeLambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Employee::getName,name).orderByDesc(Employee::getUpdateTime);
        return page(employeePage,employeeLambdaQueryWrapper);
    }
}
