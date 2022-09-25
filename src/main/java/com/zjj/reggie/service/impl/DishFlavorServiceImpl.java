package com.zjj.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjj.reggie.mapper.DishFlavorMapper;
import com.zjj.reggie.pojo.DishFlavor;
import com.zjj.reggie.service.DishFlavorService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService{
    @Resource
    private DishFlavorMapper dishFlavorMapper;
}
