package com.zjj.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjj.reggie.common.R;
import com.zjj.reggie.dto.SetmealDto;
import com.zjj.reggie.pojo.Setmeal;
import com.zjj.reggie.pojo.SetmealDish;
import com.zjj.reggie.service.SetmealDishService;
import com.zjj.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {
    @Resource
    private SetmealDishService setmealDishService;
    @Resource
    private SetmealService setmealService;

    @PostMapping
    public R<String> saveSetmeal(@RequestBody SetmealDto setmealDto){
        setmealDishService.saveWithDish(setmealDto);
        return R.success("添加成功");
    }

    @GetMapping("/page")
    public R<IPage<SetmealDto>> page(Integer page, Integer pageSize, String name){
        return R.success(setmealDishService.pageList(page,pageSize,name));
    }

    @DeleteMapping
    public R<String> delete(String ids){
        String[] split = ids.split(",");
        List<Long> collect = Arrays.stream(split).map(Long::parseLong).collect(Collectors.toList());
        setmealService.removeBatchByIds(collect);
        setmealDishService.delete(collect);
        return R.success("删除成功");
    }

    @GetMapping("/{id}")
    public R<SetmealDto> getSetmealInfo(@PathVariable Long id){
        SetmealDto setmealDto = new SetmealDto();
        Setmeal setmeal = setmealService.getById(id);
        BeanUtils.copyProperties(setmeal,setmealDto);
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(id!=null,SetmealDish::getSetmealId,id);
        List<SetmealDish> list = setmealDishService.list(setmealDishLambdaQueryWrapper);
        setmealDto.setSetmealDishes(list);
        return R.success(setmealDto);
    }

    @PutMapping
    public R<String> updateSetmeal(@RequestBody SetmealDto setmealDto){
        setmealService.removeById(setmealDto);
        Long id = setmealDto.getId();
        LambdaQueryWrapper<SetmealDish> setmealDishQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishQueryWrapper.eq(id!=null,SetmealDish::getSetmealId,id);
        setmealDishService.remove(setmealDishQueryWrapper);
        setmealDishService.saveWithDish(setmealDto);
        return R.success("修改成功");
    }

    @PostMapping("/status/{status}")
    public R<String> changeStatus(@PathVariable Integer status,String ids){
        String[] split = ids.split(",");
        List<Setmeal> collect = Arrays.stream(split).map(item -> {
            Setmeal setmeal = new Setmeal();
            setmeal.setId(Long.parseLong(item));
            setmeal.setStatus(status);
            return setmeal;
        }).collect(Collectors.toList());
        setmealService.updateBatchById(collect);
        return R.success("修改成功");
    }
}
