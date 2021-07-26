package com.joker.service;

import com.joker.pojo.Users;
import com.joker.pojo.bo.UserBO;

/**
 * 用户相关
 */
public interface UserService {

    /**
     * 方法描述: <br>
     * <p> 判断用户名是否存在 </p>
     *
     * @Author Joker
     * @CreateDate 2021/7/26 10:14
     * @param userName
     * @return boolean
     * @ReviseName
     * @ReviseTime 2021/7/26 10:14
     **/
    boolean queryUserNameIsExist(String userName);

    /**
     * 注册
     * @param userBO
     * @return
     */
    Users createUser(UserBO userBO);

    /**
     * 方法描述: <br>
     * <p> 登录 </p>
     *
     * @Author Joker
     * @CreateDate 2021/7/26 13:43
     * @param username
     * @param password
     * @return Users
     * @ReviseName
     * @ReviseTime 2021/7/26 13:43
     **/
    Users queryUserForLogin(String username , String password);
}
