package com.zjj.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjj.reggie.dto.DishDto;
import com.zjj.reggie.mapper.DishMapper;
import com.zjj.reggie.pojo.Category;
import com.zjj.reggie.pojo.Dish;
import com.zjj.reggie.pojo.DishFlavor;
import com.zjj.reggie.service.CategoryService;
import com.zjj.reggie.service.DishFlavorService;
import com.zjj.reggie.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper,Dish> implements DishService {

    @Resource
    private DishFlavorService dishFlavorService;
    @Resource
    private CategoryService categoryService;
    @Override
    @Transactional
    public void saveDish(DishDto dishDto) {
        save(dishDto);
        Long id = dishDto.getId();
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map(item -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(flavors);
    }

    @Override
    public IPage<DishDto> dishPage(Integer page, Integer pageSize, String name) {
        IPage<Dish> dishPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(name != null,Dish::getName,name);
        page(dishPage, dishLambdaQueryWrapper);
        IPage<DishDto> dishDtoPage = new Page<>();
        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        List<DishDto> dishDtos = dishPage.getRecords().stream().map(item -> {
            DishDto dishDto = new DishDto();
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            BeanUtils.copyProperties(item,dishDto);
            dishDto.setCategoryName(category.getName());
            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(dishDtos);
        return dishDtoPage;
    }

    @Override
    public DishDto getDish(Long id) {
        Dish dish = getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish,dishDto);
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(id != null,DishFlavor::getDishId,id);
        List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(dishFlavorList);
        return dishDto;
    }

    @Override
    @Transactional
    public void updateDish(DishDto dishDto) {
        updateById(dishDto);
        Long dishId = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(dishId != null,DishFlavor::getDishId,dishId);
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        dishDto.getFlavors().stream().map(item -> {
            item.setDishId(dishId);
            dishFlavorService.save(item);
            return item;
        });
    }

    @Override
    @Transactional
    public void deleteDishes(List<Long> ids) {
        removeBatchByIds(ids);
        for (Long id : ids) {
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            dishFlavorLambdaQueryWrapper.eq(id != null,DishFlavor::getDishId,id);
            dishFlavorService.remove(dishFlavorLambdaQueryWrapper);
        }
    }
}
