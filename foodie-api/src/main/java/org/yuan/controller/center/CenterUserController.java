package org.yuan.controller.center;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.yuan.controller.BaseController;
import org.yuan.pojo.Users;
import org.yuan.pojo.bo.conter.UsersBO;
import org.yuan.resource.FileUpload;
import org.yuan.service.center.UserService;
import org.yuan.utils.CookieUtils;
import org.yuan.utils.DateUtil;
import org.yuan.utils.JSONResult;
import org.yuan.utils.JsonUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Api(value = "用户信息接口",tags = "用户信息相关接口")
@RestController
@RequestMapping("userInfo")
public class CenterUserController extends BaseController {

    @Autowired
    @Qualifier("centerUserServiceImpl")
    public UserService userService;

    @Autowired
    public FileUpload fileUpload;

    @ApiOperation(value = "获取用户信息",notes = "获取用户信息",httpMethod = "POST")
    @PostMapping("update")
    public JSONResult update(
            @ApiParam(value = "userId",name = "用户id",required = true)
                    String userId,
            @RequestBody @Valid  UsersBO usersBO,
            BindingResult result,
            HttpServletRequest request,
            HttpServletResponse response){

        if(result.hasErrors()){
            JSONResult.errorMap(getErrors(result));
        }

        Users userInfo = userService.updateUserInfo(userId, usersBO);

        CookieUtils.setCookie(request,response,"user",
                JsonUtils.objectToJson(userInfo),true);

        return JSONResult.ok();
    }
    private Map<String,String> getErrors(BindingResult result){
        Map<String, String> map = new HashMap<>(result.getFieldErrors().size()* 4/3);
        result.getFieldErrors().stream().forEach( err->{
            String field = err.getField();
            String errDefaultMessage = err.getDefaultMessage();
            map.put(field,errDefaultMessage);
        });
        return map;
    }


    @ApiOperation(value = "用户头像修改",notes = "用户头像修改",httpMethod = "POST")
    @PostMapping("uploadFace")
    public JSONResult uploadFace(
            @ApiParam(value = "userId",name = "用户id",required = true)
                    String userId,
            @ApiParam(value = "file",name = "用户头像",required = true)
            MultipartFile file,
            HttpServletRequest request,
            HttpServletResponse response){

        //定义头像保存地址
        String fileSpace = fileUpload.getImageUserFaceLocation();
        //区分不同用户
        String uploadPathPrefix = File.separator + userId;

        if(!Optional.ofNullable(file).isPresent()){
            return JSONResult.errorMsg("文件不能为空");
        }

        String filename = file.getOriginalFilename();
        if(StringUtils.isBlank(filename)){
            return JSONResult.errorMsg("文件不能为空");
        }

        String[] filenameArr = filename.split("\\.");

        String suffix = filenameArr[filenameArr.length - 1];

        if(!suffix.equalsIgnoreCase("jpg") &&
                !suffix.equalsIgnoreCase("png") &&
                !suffix.equalsIgnoreCase("jpeg")
        ){
            return JSONResult.errorMsg("图片格式不正确");
        }

        //重新定义文件名
        String newFileName = "face-" + userId + "." + suffix;

        //上传头像最终路径
        String finalFacePath = fileSpace + uploadPathPrefix + File.separator + newFileName;

        File outFile = new File(finalFacePath);
        if(null != outFile.getParentFile()){
            outFile.getParentFile().mkdirs();
        }



        try(FileOutputStream fileOutputStream = new FileOutputStream(outFile);
            InputStream inputStream = file.getInputStream()) {
            IOUtils.copy(inputStream,fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String faceUrl = fileUpload.getImageServerUrl() + "/" +userId + "/" + newFileName+
                "?t="+ DateUtil.getCurrentDateString(DateUtil.DATE_PATTERN);

        Users users = userService.updateUserFace(userId, faceUrl);

        CookieUtils.setCookie(request,response,"user",
                JsonUtils.objectToJson(users),true);
        return JSONResult.ok(users);
    }
}
