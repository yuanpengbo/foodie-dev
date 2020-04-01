package org.yuan.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yuan.pojo.UserAddress;
import org.yuan.pojo.bo.AddressBO;
import org.yuan.service.AddressService;
import org.yuan.utils.JSONResult;
import org.yuan.utils.MobileEmailUtils;
import springfox.documentation.spring.web.json.Json;

import java.util.List;

@Api(value = "地址相关",tags = {"地址相关的接口"})
@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    AddressService addressService;

    @ApiOperation(value = "根据用户Id查询地址",notes = "根据用户Id查询地址",httpMethod = "POST")
    @PostMapping("/list")
    public JSONResult list(
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @RequestParam String userId){

        if(StringUtils.isBlank(userId)){
            JSONResult.errorMsg("");
        }

        List<UserAddress> userAddresses = addressService.queryAll(userId);
        return JSONResult.ok(userAddresses);
    }

    @ApiOperation(value = "修改地址",notes = "修改地址",httpMethod = "POST")
    @PostMapping("/update")
    public JSONResult update(@RequestBody AddressBO addressBO){

        if(StringUtils.isBlank(addressBO.getAddressId())){
            return JSONResult.errorMsg("修改地址错误：addressId 不能为空");
        }

        JSONResult CheckResult = checkAddressBO(addressBO);
        if(!CheckResult.isOK()){
            return CheckResult;
        }
        addressService.updateUserAddress(addressBO);
        return JSONResult.ok();
    }

    @ApiOperation(value = "新增地址",notes = "新增地址",httpMethod = "POST")
    @PostMapping("/add")
    public JSONResult add(@RequestBody AddressBO addressBO){
        JSONResult CheckResult = checkAddressBO(addressBO);
        if(!CheckResult.isOK()){
            return CheckResult;
        }

        addressService.addNewUserAddress(addressBO);
        return JSONResult.ok();
    }

    @ApiOperation(value = "新增地址",notes = "新增地址",httpMethod = "POST")
    @PostMapping("/delete")
    public JSONResult delete(
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @Param("userId") String userId,
            @ApiParam(name = "addressId",value = "地址ID",required = true)
            @Param("addressId") String addressId){

        if(StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)){
            return JSONResult.errorMsg("");
        }

        addressService.deleteUserAddress(userId,addressId);
        return JSONResult.ok();
    }

    @ApiOperation(value = "设置默认地址",notes = "设置默认地址",httpMethod = "POST")
    @PostMapping("/setDefault")
    public JSONResult setDefault(
            @ApiParam(name = "userId",value = "用户ID",required = true)
            @Param("userId") String userId,
            @ApiParam(name = "addressId",value = "地址ID",required = true)
            @Param("addressId") String addressId){

        if(StringUtils.isBlank(userId) || StringUtils.isBlank(addressId)){
            return JSONResult.errorMsg("");
        }
        addressService.updateUserAddressToBeDefault(userId,addressId);
        return JSONResult.ok();
    }

    private JSONResult checkAddressBO(AddressBO addressBO){
        String receiver = addressBO.getReceiver();
        if (StringUtils.isBlank(receiver)){
            return JSONResult.errorMsg("收货人不能为空");
        }

        if (receiver.length() > 12){
            return JSONResult.errorMsg("收货人姓名不能台词");
        }

        String mobile = addressBO.getMobile();
        if (StringUtils.isBlank(mobile)){
            return JSONResult.errorMsg("收货人手机号不能为空");
        }

        if (mobile.length() != 11){
            return JSONResult.errorMsg("收获人手机号长度不正确");
        }

        if (MobileEmailUtils.checkMobileIsOk(mobile)){
            return JSONResult.errorMsg("收货人手机格式不正确");
        }

        String province = addressBO.getProvince();
        String city = addressBO.getCity();
        String district = addressBO.getDistrict();
        String detail = addressBO.getDetail();
        if (StringUtils.isBlank(province) || StringUtils.isBlank(city)
                || StringUtils.isBlank(district) || StringUtils.isBlank(detail)){
            return JSONResult.errorMsg("收货人地址信息不能为空");
        }
        return JSONResult.ok();
    }
}
