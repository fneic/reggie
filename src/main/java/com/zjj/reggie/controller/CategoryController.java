package com.zjj.reggie.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjj.reggie.common.R;
import com.zjj.reggie.exception.CategoryDeleteException;
import com.zjj.reggie.pojo.Category;
import com.zjj.reggie.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryService categoryService;

    @PostMapping
    public R<String> saveCategory(@RequestBody Category category){
        categoryService.save(category);
        return R.success("添加成功");
    }

    @GetMapping("/page")
    public R<IPage<Category>> getCategoryPage(Integer page,Integer pageSize){
        return R.success(categoryService.categoryPage(page,pageSize));
    }

    @DeleteMapping
    public R<String> deleteCategory(Long ids) throws CategoryDeleteException {
        boolean res = categoryService.deleteCategory(ids);
        if(res)
            return R.success("删除成功");
        return R.error("删除失败");
    }

    @PutMapping
    public R<String> updateCategory(@RequestBody Category category){
        boolean res = categoryService.updateById(category);
        if(res)
            return R.success("修改成功");
        return R.error("修改失败");
    }

    @GetMapping("/list")
    public R<List<Category>> categoryList(Category category){
        List<Category> categories = categoryService.listCategory(category);
        return R.success(categories);
    }
}
