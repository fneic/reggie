package com.zjj.reggie.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjj.reggie.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
