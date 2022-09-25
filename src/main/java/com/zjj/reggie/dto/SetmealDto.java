package com.zjj.reggie.dto;

import com.zjj.reggie.pojo.Setmeal;
import com.zjj.reggie.pojo.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
