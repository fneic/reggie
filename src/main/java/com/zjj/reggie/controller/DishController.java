package com.zjj.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjj.reggie.common.R;
import com.zjj.reggie.dto.DishDto;
import com.zjj.reggie.pojo.Category;
import com.zjj.reggie.pojo.Dish;
import com.zjj.reggie.service.DishService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Resource
    private DishService dishService;

    @PostMapping
    private R<String> saveDish(@RequestBody DishDto dishDto){
        dishService.saveDish(dishDto);
        return R.success("添加成功");
    }

    @GetMapping("/page")
    private R<IPage<DishDto>> getDishPage(Integer page, Integer pageSize, String name){
        return R.success(dishService.dishPage(page, pageSize, name));
    }

    @GetMapping("/{id}")
    private R<DishDto> getDish(@PathVariable Long id){
        return R.success(dishService.getDish(id));
    }

    @PutMapping
    private R<String> updateWithFlavor(@RequestBody DishDto dishDto){
        dishService.updateDish(dishDto);
        return R.success("修改成功");
    }

    @DeleteMapping
    private R<String> deleteDishes(String ids){
        List<String> idsList = Arrays.asList(ids.split(","));
        List<Long> collect = idsList.stream().map(Long::parseLong).collect(Collectors.toList());
        dishService.deleteDishes(collect);
        return R.success("删除成功");
    }

    @PostMapping("/status/{status}")
    private R<String> updateStatus(@PathVariable Integer status,String ids){
        List<String> idsList = Arrays.asList(ids.split(","));
        List<Dish> collect = idsList.stream().map(item -> {
            Dish dish = new Dish();
            dish.setId(Long.parseLong(item));
            dish.setStatus(status);
            return dish;
        }).collect(Collectors.toList());
        dishService.updateBatchById(collect);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish){
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId()).eq(Dish::getStatus,1).orderByAsc(Dish::getSort).orderByDesc(Dish::getCreateTime);
        return R.success(dishService.list(dishLambdaQueryWrapper));
    }
}
