package com.joker.service.impl.center;

import com.joker.mapper.UsersMapper;
import com.joker.pojo.Users;
import com.joker.pojo.bo.center.CenterUserBO;
import com.joker.service.center.CenterUserService;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Service
public class CenterUserServiceImpl implements CenterUserService {

    public final UsersMapper usersMapper;

    private final Sid sid;

    public CenterUserServiceImpl(UsersMapper usersMapper, Sid sid) {
        this.usersMapper = usersMapper;
        this.sid = sid;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Users queryUserInfo(String userId) {
        Users user = usersMapper.selectByPrimaryKey(userId);
        user.setPassword(null);
        return user;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {

        Users updateUser = new Users();
        BeanUtils.copyProperties(centerUserBO, updateUser);
        updateUser.setId(userId);
        updateUser.setUpdatedTime(new Date());

        usersMapper.updateByPrimaryKeySelective(updateUser);

        return queryUserInfo(userId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public Users updateUserFace(String userId, String faceUrl) {
        Users updateUser = new Users();
        updateUser.setId(userId);
        updateUser.setFace(faceUrl);
        updateUser.setUpdatedTime(new Date());

        usersMapper.updateByPrimaryKeySelective(updateUser);

        return queryUserInfo(userId);
    }
}
