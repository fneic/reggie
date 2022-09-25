package com.zjj.reggie.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjj.reggie.exception.CategoryDeleteException;
import com.zjj.reggie.pojo.Category;

import java.util.List;

public interface CategoryService extends IService<Category> {
    IPage<Category> categoryPage(Integer page,Integer pageSize);
    boolean deleteCategory(Long ids) throws CategoryDeleteException;
    List<Category> listCategory(Category category);
}
