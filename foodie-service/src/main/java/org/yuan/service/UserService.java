package org.yuan.service;


import org.yuan.pojo.Users;
import org.yuan.pojo.bo.UserBO;

public interface UserService {

    /**
     * 判断用户名是否存在
     */
    boolean queryUsernameIsExist(String username);

    Users createUser(UserBO userBO);

    Users queryUserForLogin(String username,String password);
}
