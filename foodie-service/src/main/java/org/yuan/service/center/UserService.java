package org.yuan.service.center;

import org.yuan.pojo.Users;
import org.yuan.pojo.bo.conter.UsersBO;

public interface UserService {
    Users queryUserInfo(String userId);

    Users updateUserInfo(String userId , UsersBO  usersBO);

    Users updateUserFace(String userId , String  faceUrl);
}
