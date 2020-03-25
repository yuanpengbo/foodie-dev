package org.yuan.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yuan.pojo.bo.UserBO;
import org.yuan.service.UserService;
import org.yuan.utils.JSONResult;

@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    public UserService userService;

    @GetMapping("/usernameIsExist")
    public JSONResult usernameIsExist(@RequestParam String username){

        //1.判断用户名不能为空
        if(StringUtils.isBlank(username)){
            return JSONResult.errorMsg("用户名不能为空");
        }

        //2.查找注册的用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);

        if(isExist){
            JSONResult.errorMsg("用户名已存在");;
        }
        //3.请求成功，用户没有重复
        return JSONResult.ok();
    }

    @PostMapping("/regist")
    public JSONResult regist(@RequestBody UserBO userBO){

        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();


        //0.判断用户密码不能为空
        if (StringUtils.isBlank(username)
                || StringUtils.isBlank(password)
                || StringUtils.isBlank(confirmPassword)){
            return JSONResult.errorMsg("用户名密码不能为空");
        }

        //1.查找注册的用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if(isExist){
            return JSONResult.errorMsg("用户名已存在");
        }

        //2.判断密码长度不能小于6位
        if (password.length() < 6){
            return JSONResult.errorMsg("密码长度不能小于6位");
        }

        //3.判断两次密码是否一致
        if(!password.equals(confirmPassword)){
            return JSONResult.errorMsg("两次密码不一致");
        }

        //4.实现注册
        userService.createUser(userBO);
        return JSONResult.ok();
    }
}
