package org.yuan.service.center.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yuan.mapper.UsersMapper;
import org.yuan.pojo.Users;
import org.yuan.pojo.bo.conter.UsersBO;
import org.yuan.service.center.UserService;

import java.util.Date;

@Service("centerUserServiceImpl")
public class UserServiceImpl implements UserService {

    public UserServiceImpl(@Autowired  UsersMapper usersMapper){
        this.usersMapper = usersMapper;
    }


    private final UsersMapper usersMapper;


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        users.setPassword(null);
        return users;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, UsersBO usersBO) {
        Users users = new Users();
        BeanUtils.copyProperties(usersBO,users);
        users.setId(userId);
        users.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(users);

        return queryUserInfo(userId);
    }

    @Override
    public Users updateUserFace(String userId, String faceUrl) {
        Users users = new Users();
        users.setId(userId);
        users.setFace(faceUrl);
        users.setUpdatedTime(new Date());
        usersMapper.updateByPrimaryKeySelective(users);
        return queryUserInfo(userId);
    }
}
