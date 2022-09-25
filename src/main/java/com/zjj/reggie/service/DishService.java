package com.zjj.reggie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjj.reggie.dto.DishDto;
import com.zjj.reggie.pojo.Dish;

import java.util.List;


public interface DishService extends IService<Dish> {
    void saveDish(DishDto dishDto);
    IPage<DishDto> dishPage(Integer page, Integer pageSize, String name);
    DishDto getDish(Long id);
    void updateDish(DishDto dishDto);
    void deleteDishes(List<Long> ids);
}
