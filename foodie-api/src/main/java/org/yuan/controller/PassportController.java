package org.yuan.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yuan.pojo.Users;
import org.yuan.pojo.bo.UserBO;
import org.yuan.service.UserService;
import org.yuan.utils.CookieUtils;
import org.yuan.utils.JSONResult;
import org.yuan.utils.JsonUtils;
import org.yuan.utils.MD5Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Api(value = "注册登录",tags = {"用户注册登录相关接口"})
@RestController
@RequestMapping("passport")
public class PassportController {

    @Autowired
    public UserService userService;

    @ApiOperation(value = "用户名是否存在",notes = "用户名是否存在1",httpMethod = "GET")
    @GetMapping("/usernameIsExist")
    public JSONResult usernameIsExist(@RequestParam String username){

        //1.判断用户名不能为空
        if(StringUtils.isBlank(username)){
            return JSONResult.errorMsg("用户名不能为空");
        }

        //2.查找注册的用户名是否存在
        boolean isExist = userService.queryUsernameIsExist(username);
        if(isExist){
            return JSONResult.errorMsg("用户名已存在");
        }

        //3.请求成功，用户没有重复
        return JSONResult.ok();
    }

    @ApiOperation(value = "用户注册",notes = "用户注册1",httpMethod = "POST")
    @PostMapping("/regist")
    public JSONResult regist(@RequestBody UserBO userBO,
                             HttpServletRequest request,
                             HttpServletResponse response){

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
        Users user = userService.createUser(userBO);

        CookieUtils.setCookie(request,response,"user",
                JsonUtils.objectToJson(user),true);

        return JSONResult.ok();
    }

    @ApiOperation(value = "用户登录",notes = "用户登录",httpMethod = "POST")
    @PostMapping("/login")
    public JSONResult login(@RequestBody UserBO userBO,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {

        String username = userBO.getUsername();
        String password = userBO.getPassword();


        //1.判断用户密码不能为空
        if (StringUtils.isBlank(username)
                || StringUtils.isBlank(password)){
            return JSONResult.errorMsg("用户名密码不能为空");
        }

        //4.实现登录
        Users user = userService.queryUserForLogin(username,
                MD5Utils.getMD5Str(password));


        CookieUtils.setCookie(request,response,"user",
                JsonUtils.objectToJson(user),true);
        //判断是否登录成功
        if (!Optional.ofNullable(user).isPresent()){
            return JSONResult.errorMsg("用户名密码不正确");
        }

        return JSONResult.ok(user);
    }

    @ApiOperation(value = "用户登录",notes = "用户登录",httpMethod = "POST")
    @PostMapping("/logout")
    public JSONResult login(@RequestParam String userId,
                            HttpServletRequest request,
                            HttpServletResponse response) throws Exception {
        // 清除cookie
        CookieUtils.deleteCookie(request,response,"user");

        //TODO 清除购物车
        //TODO 清除分布式用户信息
        return JSONResult.ok();
    }

}
