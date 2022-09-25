package com.zjj.reggie.common;

import com.zjj.reggie.exception.CategoryDeleteException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice(annotations = {Controller.class, RestController.class})
@Slf4j
public class GlobalExceptions {

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    @ResponseBody
    public R<String> UserNameRepeatExceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info(ex.getMessage());
        if(ex.getMessage().contains("Duplicate entry")){
            return R.error("数据已存在，修改数据失败");
        }
        return R.error("服务器繁忙");
    }

    @ExceptionHandler(CategoryDeleteException.class)
    @ResponseBody
    public R<String> CategoryDeleteExceptionHandler(CategoryDeleteException ex){
        log.info(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
