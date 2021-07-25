package com.joker.service.impl;

import com.joker.mapper.UsersMapper;
import com.joker.pojo.Users;
import com.joker.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;


@Service
public class UserServiceImpl implements UserService {

    private final UsersMapper usersMapper;

    public UserServiceImpl(UsersMapper usersMapper) {
        this.usersMapper = usersMapper;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public boolean queryUserNameIsExist(String userName) {
        Example example = new Example(Users.class);
        Example.Criteria criteria = example.createCriteria();
        Example.Criteria username = criteria.andEqualTo("username", userName);
        Users users = usersMapper.selectOneByExample(username);
        return users == null;
    }
}
