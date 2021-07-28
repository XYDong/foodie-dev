package com.joker.controller;

import com.joker.pojo.Users;
import com.joker.pojo.bo.UserBO;
import com.joker.service.UserService;
import com.joker.utils.CookieUtils;
import com.joker.utils.JSONResult;
import com.joker.utils.JsonUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author shuiq
 */
@Api(value = "注册登录", tags = "用于注册登录的相关接口")
@RestController
@RequestMapping("passport")
public class PassPortController {

    private final UserService userService;

    public PassPortController(UserService userService) {
        this.userService = userService;
    }

    @ApiOperation(value = "用户名是否存在", httpMethod = "GET",notes = "用于检查用户名是否重复")
    @GetMapping("usernameIsExist")
    public JSONResult usernameIsExist(@RequestParam String username){
        // 判断入参不能为空
        if (StringUtils.isBlank(username)) {
            return JSONResult.errorMsg("用户名不能为空");
        }
        // 查找注册的用户名是否存在
        boolean isExist = userService.queryUserNameIsExist(username);
        return !isExist ? JSONResult.ok() : JSONResult.errorMsg("用户名已经存在");
    }

    @ApiOperation(value = "注册", httpMethod = "POST",notes = "用户注册")
    @PostMapping("regist")
    public JSONResult regist(@RequestBody UserBO userBO, HttpServletRequest request,HttpServletResponse response){
        // 判断入参
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        String confirmPassword = userBO.getConfirmPassword();
        // 判断用户名或密码是否为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password) || StringUtils.isBlank(confirmPassword)) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }
        // 查询用户名是否存在
        boolean isExist = userService.queryUserNameIsExist(username);
        if (isExist) {
            return JSONResult.errorMsg("用户名已经存在");
        }
        // 密码长度不能少于6位
        if (password.length() < 6) {
            return JSONResult.errorMsg("密码长度不能少于6位");
        }
        // 两次输入的密码是否一致
        if (!password.equals(confirmPassword)) {
            return JSONResult.errorMsg("两次密码输入不一致");
        }
        // 实现注册
        Users users = userService.createUser(userBO);
        setNullProperty(users);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);
        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据
        return JSONResult.ok();
    }


    @ApiOperation(value = "用户登录", httpMethod = "POST",notes = "用户登录")
    @PostMapping("login")
    public JSONResult login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response){
        // 判断入参
        String username = userBO.getUsername();
        String password = userBO.getPassword();
        // 判断用户名或密码是否为空
        if (StringUtils.isBlank(username) || StringUtils.isBlank(password)) {
            return JSONResult.errorMsg("用户名或密码不能为空");
        }
        // 实现登录
        Users users = userService.queryUserForLogin(username, password);
        if (users == null) {
            return JSONResult.errorMsg("用户名或密码错误");
        }
        setNullProperty(users);
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);
        // TODO 生成用户token，存入redis会话
        // TODO 同步购物车数据
        return JSONResult.ok(users);
    }

    /**
     * 方法描述: <br>
     * <p> 设置默认值 </p>
     *
     * @Author Joker
     * @CreateDate 2021/7/26 14:36
     * @param user 用户信息
     * @ReviseName
     * @ReviseTime 2021/7/26 14:36
     **/
    private void setNullProperty(Users user) {
        user.setPassword(null);
        user.setMobile(null);
        user.setEmail(null);
        user.setCreatedTime(null);
        user.setUpdatedTime(null);
        user.setBirthday(null);
    }

    @ApiOperation(value = "退出登录", httpMethod = "POST",notes = "用户退出登录")
    @PostMapping("logout")
    public JSONResult logout(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response){
        // 清除用户cookie信息
        CookieUtils.deleteCookie(request,response,"user");
        // TODO 用户退出登录，清空购物车
        // TODO 分布式会话中需要清除用户数据

        return JSONResult.ok();
    }


}
