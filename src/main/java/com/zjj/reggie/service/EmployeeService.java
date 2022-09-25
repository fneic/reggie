package com.zjj.reggie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjj.reggie.common.R;
import com.zjj.reggie.pojo.Employee;


public interface EmployeeService extends IService<Employee> {

    R<Employee> checkLogin(Employee employee);
    R<String> addNewEmployee(Employee employee);
    IPage<Employee> getEmployeePage(Integer page,Integer pageSize,String name);
}
