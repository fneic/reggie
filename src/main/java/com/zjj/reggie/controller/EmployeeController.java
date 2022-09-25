package com.zjj.reggie.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjj.reggie.common.R;
import com.zjj.reggie.pojo.Employee;
import com.zjj.reggie.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Resource
    EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee){
       R<Employee> res = employeeService.checkLogin(employee);
       if(res.getCode() == 1)
           request.getSession().setAttribute("employee",res.getData().getId());
       return res;
    }

    @PostMapping("/logout")
    public R<Employee> logout(HttpServletRequest request){
        request.getSession().removeAttribute("employee");
        return R.success(null);
    }

    @PostMapping
    public R<String> addNewEmployee(@RequestBody Employee employee,HttpServletRequest request){
        Long creatorId = (Long)request.getSession().getAttribute("employee");
        employee.setCreateUser(creatorId);
        employee.setUpdateUser(creatorId);
        return employeeService.addNewEmployee(employee);
    }

    @GetMapping("/page")
    public R<IPage<Employee>> getPage(Integer page,Integer pageSize,String name){
        return R.success(employeeService.getEmployeePage(page,pageSize,name));
    }

    @PutMapping
    public R<String> updateEmployeeInfo(@RequestBody Employee employee,HttpServletRequest request){
        Long updateUser = (Long) request.getSession().getAttribute("employee");
        employeeService.updateById(employee);
        return R.success("修改成功");
    }

    @GetMapping("/{id}")
    public R<Employee> getEmployee(@PathVariable Long id){
        return R.success(employeeService.getById(id));
    }
}
