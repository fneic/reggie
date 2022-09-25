package com.zjj.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjj.reggie.exception.CategoryDeleteException;
import com.zjj.reggie.mapper.CategoryMapper;
import com.zjj.reggie.mapper.DishMapper;
import com.zjj.reggie.mapper.SetmealMapper;
import com.zjj.reggie.pojo.Category;
import com.zjj.reggie.pojo.Dish;
import com.zjj.reggie.pojo.Setmeal;
import com.zjj.reggie.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private DishMapper dishMapper;
    @Resource
    private SetmealMapper setmeal;

    @Override
    public IPage<Category> categoryPage(Integer page, Integer pageSize) {
        IPage<Category> categoryPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.orderByAsc(Category::getSort);
        this.page(categoryPage,categoryLambdaQueryWrapper);
        return categoryPage;
    }

    @Override
    public boolean deleteCategory(Long ids) throws CategoryDeleteException {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId,ids);
        Long dishSize = dishMapper.selectCount(dishLambdaQueryWrapper);
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,ids);
        Long setmealSize = setmeal.selectCount(setmealLambdaQueryWrapper);
        if(dishSize > 0){
            throw new CategoryDeleteException("该分类有关联菜品，无法删除");
        }
        if(setmealSize > 0){
            throw  new CategoryDeleteException("该分类有关联套餐，无法删除");
        }
        return categoryMapper.deleteById(ids) > 0;
    }

    @Override
    public List<Category> listCategory(Category category) {
        LambdaQueryWrapper<Category> categoryLambdaQueryWrapper = new LambdaQueryWrapper<>();
        categoryLambdaQueryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        return list(categoryLambdaQueryWrapper);
    }


}
