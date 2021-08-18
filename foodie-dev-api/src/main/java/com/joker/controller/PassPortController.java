package com.joker.controller;

import com.joker.pojo.Users;
import com.joker.pojo.bo.ShopcartBO;
import com.joker.pojo.bo.UserBO;
import com.joker.pojo.vo.UsersVO;
import com.joker.service.UserService;
import com.joker.utils.CookieUtils;
import com.joker.utils.JSONResult;
import com.joker.utils.JsonUtils;
import com.joker.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author shuiq
 */
@Api(value = "注册登录", tags = "用于注册登录的相关接口")
@RestController
@RequestMapping("passport")
public class PassPortController extends BaseController {

    private final UserService userService;

    private final RedisOperator redisOperator;

    public PassPortController(UserService userService, RedisOperator redisOperator) {
        this.userService = userService;
        this.redisOperator = redisOperator;
    }

    @ApiOperation(value = "用户名是否存在", httpMethod = "GET", notes = "用于检查用户名是否重复")
    @GetMapping("usernameIsExist")
    public JSONResult usernameIsExist(@RequestParam String username) {
        // 判断入参不能为空
        if (StringUtils.isBlank(username)) {
            return JSONResult.errorMsg("用户名不能为空");
        }
        // 查找注册的用户名是否存在
        boolean isExist = userService.queryUserNameIsExist(username);
        return !isExist ? JSONResult.ok() : JSONResult.errorMsg("用户名已经存在");
    }

    @ApiOperation(value = "注册", httpMethod = "POST", notes = "用户注册")
    @PostMapping("regist")
    public JSONResult regist(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
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
        Users userResult = userService.createUser(userBO);
//        setNullProperty(userResult);
        // 生成用户token，存入redis会话
        UsersVO usersVO = userConvertVO(userResult);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);
        // 同步购物车数据
        syncShopcartData(userResult.getId(),request,response);
        return JSONResult.ok();
    }


    /**
     * 方法描述: <br>
     * <p> 注册登录成功后，同步cookie和redis中的购物车数据 </p>
     *
     * @param userId 用户id
     * @param request 请求
     * @param response 返回
     * @return void
     * @Author Joker
     * @CreateDate 2021/8/6 10:43
     * @ReviseName
     * @ReviseTime 2021/8/6 10:43
     **/
    private void syncShopcartData(String userId, HttpServletRequest request, HttpServletResponse response) {
        /**
         * 1. redis中无数据，如果cookie中的购物车为空，则不作任何处理
         *                  如果cookie中购物车不为空，则同步到redis中
         * 2. redis中有数据，如果cookie中的购物车为空，则同步redis中的数据到cookie
         *                 如果cookie中购物车不为空，如果cookie中的某个商品在redis中存在，则删除redis中的，把cookie中的商品直接覆盖到redis中（参考京东）
         * 3. 同步到redis汇总去了以后，覆盖本地cookie购物车数据，保证本地购物车数据是最新的
         */
        // 从redis中获取购物车
        String shopcartReis = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
        // 从cookie中获取购物车
        String shopcartCookie = CookieUtils.getCookieValue(request, FOODIE_SHOPCART, true);

        if (StringUtils.isBlank(shopcartReis)) {
            // redis为空，cookie不为空，直接把cookie中的数据放入redis
            if (StringUtils.isNotBlank(shopcartCookie)) {
                redisOperator.set(FOODIE_SHOPCART+":"+userId,shopcartCookie);
            }

        } else {
            // redis不为空，cookie不为空，合并cookie和redis中购物车数据（同一商品则覆盖redis）
            if (StringUtils.isNotBlank(shopcartCookie)) {
                /**
                 * 1. 已经存在的，把cookie中对应的数量，父爱redis（参考京东）
                 * 2. 该项商品标记为待删除，统一放入一个待删除的list
                 * 3. 从cookie汇总清理所有待删除list
                 * 4. 合并redis和cookie中的数据
                 * 5. 更新redis和cookie中
                 */
                List<ShopcartBO> shopcartRedisList = JsonUtils.jsonToList(shopcartReis, ShopcartBO.class);
                List<ShopcartBO> shopcartCookieList = JsonUtils.jsonToList(shopcartCookie, ShopcartBO.class);
                // 定义一个待删除的list
                List<ShopcartBO> pendingDeleteList = new ArrayList<>();
                for (ShopcartBO redisShop : shopcartRedisList) {
                    String redisShopSpecId = redisShop.getSpecId();
                    for (ShopcartBO cookieShop : shopcartCookieList) {
                        String cookieShopSpecId = cookieShop.getSpecId();
                        if (redisShopSpecId.equals(cookieShopSpecId)) {
                            // 覆盖购买数量，不累加，参考静定
                            redisShop.setBuyCounts(cookieShop.getBuyCounts());
                            // 把cookieshopcart放入待删除列表，用于最后的删除与合并
                            pendingDeleteList.add(cookieShop);
                        }
                    }
                }
                // 从现有cookie中删除对应的覆盖过的商品数据
                shopcartCookieList.removeAll(pendingDeleteList);
                // 合并两个list
                shopcartRedisList.addAll(shopcartCookieList);
                // 更新redis到cookie
                CookieUtils.setCookie(request,response,FOODIE_SHOPCART,JsonUtils.objectToJson(shopcartRedisList),true);
                // 同步redis
                redisOperator.set(FOODIE_SHOPCART+":" + userId,JsonUtils.objectToJson(shopcartRedisList));
            } else {
                // redis 不为空，cookie为空，直接把redis覆盖cookie
                CookieUtils.setCookie(request,response,FOODIE_SHOPCART,shopcartReis,true);
            }
        }

    }

    @ApiOperation(value = "用户登录", httpMethod = "POST", notes = "用户登录")
    @PostMapping("login")
    public JSONResult login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) {
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
        UsersVO usersVO = userConvertVO(users);
        CookieUtils.setCookie(request, response, "user", JsonUtils.objectToJson(usersVO), true);
        // 生成用户token，存入redis会话
        // 同步购物车数据
        syncShopcartData(users.getId(),request,response);
        return JSONResult.ok(users);
    }

    /**
     * 方法描述: <br>
     * <p> 设置默认值 </p>
     *
     * @param user 用户信息
     * @Author Joker
     * @CreateDate 2021/7/26 14:36
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

    @ApiOperation(value = "退出登录", httpMethod = "POST", notes = "用户退出登录")
    @PostMapping("logout")
    public JSONResult logout(@RequestParam String userId, HttpServletRequest request, HttpServletResponse response) {
        // 清除用户cookie信息
        CookieUtils.deleteCookie(request, response, "user");
        // 分布式会话中需要清除用户数据
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
        // 用户退出登录，清空购物车
        CookieUtils.deleteCookie(request,response,FOODIE_SHOPCART);
        return JSONResult.ok();
    }


}
