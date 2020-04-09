package org.yuan.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yuan.pojo.Users;
import org.yuan.service.center.UserService;
import org.yuan.utils.JSONResult;

@Api(value = "center-用户中心",tags ="用户中心相关接口" )
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    @Qualifier("centerUserServiceImpl")
    public UserService userService;


    @ApiOperation(value = "获取用户信息",notes = "获取用户信息",httpMethod = "POST")
    @GetMapping("userInfo")
    public JSONResult userInfo(
            @ApiParam(value = "userId",name = "用户id",required = true)
            String userId){
        System.out.println(HttpMethod.POST.toString());
        Users userInfo = userService.queryUserInfo(userId);
        return JSONResult.ok(userInfo);
    }
}
