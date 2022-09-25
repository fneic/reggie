package com.zjj.reggie.controller;

import com.zjj.reggie.common.R;
import com.zjj.reggie.pojo.User;
import com.zjj.reggie.service.UserService;
import com.zjj.reggie.utils.SMSUtils;
import com.zjj.reggie.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMessageCode(@RequestBody User user){
        Integer integer = ValidateCodeUtils.generateValidateCode(4);
        log.info(integer.toString());
        SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",user.getPhone(),integer.toString());
        return R.success("验证码发送成功");
    }
}
