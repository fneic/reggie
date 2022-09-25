package com.zjj.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjj.reggie.dto.SetmealDto;
import com.zjj.reggie.mapper.SetmealDishMapper;
import com.zjj.reggie.mapper.SetmealMapper;
import com.zjj.reggie.pojo.Setmeal;
import com.zjj.reggie.pojo.SetmealDish;
import com.zjj.reggie.service.CategoryService;
import com.zjj.reggie.service.SetmealDishService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
    @Resource
    private SetmealMapper setmealMapper;
    @Resource
    private CategoryService categoryService;
    @Override
    public void saveWithDish(SetmealDto setmealDto) {
        setmealMapper.insert(setmealDto);
        Long id = setmealDto.getId();
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes = setmealDishes.stream().map(item -> {
            item.setSetmealId(id);
            return item;
        }).collect(Collectors.toList());
        saveBatch(setmealDishes);
    }

    @Override
    public IPage<SetmealDto> pageList(Integer page, Integer pageSize, String name) {
        IPage<Setmeal> setmealPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.like(name!=null,Setmeal::getName,name);
        setmealMapper.selectPage(setmealPage,setmealLambdaQueryWrapper);
        IPage<SetmealDto> setmealDtoPage = new Page<>();
        BeanUtils.copyProperties(setmealPage,setmealDtoPage,"records");
        List<SetmealDto> collect = setmealPage.getRecords().stream().map(item -> {
            SetmealDto setmealDto = new SetmealDto();
            String categoryName = categoryService.getById(item.getCategoryId()).getName();
            BeanUtils.copyProperties(item, setmealDto);
            setmealDto.setCategoryName(categoryName);
            return setmealDto;
        }).collect(Collectors.toList());
        setmealDtoPage.setRecords(collect);
        return setmealDtoPage;
    }

    @Override
    public void delete(List<Long> ids) {
        for (Long id : ids) {
            LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
            setmealDishLambdaQueryWrapper.eq(id!=null,SetmealDish::getSetmealId,id);
            remove(setmealDishLambdaQueryWrapper);
        }
    }
}
