package com.joker.service.impl;

import com.joker.enums.Sex;
import com.joker.mapper.UsersMapper;
import com.joker.pojo.Users;
import com.joker.pojo.bo.UserBO;
import com.joker.service.UserService;
import com.joker.utils.DateUtil;
import com.joker.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;


@Service
public class UserServiceImpl implements UserService {

    private final UsersMapper usersMapper;
    private static final String USER_FACE = "https://bpic.588ku.com/element_origin_min_pic/19/03/07/4e24eb2b947fbe5113f69f02b5918c9d.jpg";
    private final Sid sid;
    public UserServiceImpl(UsersMapper usersMapper, Sid sid) {
        this.usersMapper = usersMapper;
        this.sid = sid;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserNameIsExist(String userName) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", userName);
        Users users = usersMapper.selectOneByExample(example);
        return users != null;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users createUser(UserBO userBO) {
        Users users = new Users();
        users.setId(sid.nextShort());
        users.setUsername(userBO.getUsername());
        try {
            users.setPassword(MD5Utils.getMD5Str(userBO.getPassword()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        users.setNickname(userBO.getUsername());
        users.setFace(USER_FACE);
        users.setBirthday(DateUtil.stringToDate("1900-01-01"));
        users.setSex(Sex.secret.type);
        users.setCreatedTime(new Date());
        users.setUpdatedTime(new Date());
        usersMapper.insert(users);
        return users;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserForLogin(String username, String password) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("username", username);
        criteria.andEqualTo("password", password);
        return usersMapper.selectOneByExample(example);
    }
}
