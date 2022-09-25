package com.zjj.reggie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjj.reggie.dto.SetmealDto;
import com.zjj.reggie.pojo.Setmeal;
import com.zjj.reggie.pojo.SetmealDish;
import java.util.List;


public interface SetmealDishService extends IService<SetmealDish> {

    void saveWithDish(SetmealDto setmealDto);
    IPage<SetmealDto> pageList(Integer page, Integer pageSize, String name);
    void delete(List<Long> ids);
}
