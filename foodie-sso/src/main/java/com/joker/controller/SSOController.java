package com.joker.controller;

import com.joker.pojo.Users;
import com.joker.pojo.vo.UsersVO;
import com.joker.service.UserService;
import com.joker.utils.JSONResult;
import com.joker.utils.JsonUtils;
import com.joker.utils.MD5Utils;
import com.joker.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @version 1.0.0
 * @ClassName HelloController.java
 * @Package com.joker.controller
 * @Author Joker
 * @Description
 * @CreateTime 2021年07月23日 13:57:00
 */
@Controller
public class SSOController {

    @Autowired
    private UserService userService;
    @Autowired
    private RedisOperator redisOperator;

    public static final String REDIS_USER_TOKEN = "redis_user_token";
    public static final String REDIS_USER_TICKET = "redis_user_ticket";
    public static final String REDIS_TMP_TICKET = "redis_tmp_ticket";
    public static final String COOKIE_USER_TICKET = "cookie_user_ticket";




    @GetMapping("/hello")
    @ResponseBody
    public Object hello(){
        return "hello world!!!";
    }


    @GetMapping("/login")
    public Object login(String returnUrl,
                        Model model,
                        HttpServletRequest request,
                        HttpServletResponse response){
        model.addAttribute("returnUrl",returnUrl);
        // 1. 获取userTicket门票，如果cookie中能获取到，证明用户登录过，此时签发一个一次性的临时票据
        String userTicket = getCookie(COOKIE_USER_TICKET, request);
        boolean isVerify = verifyUserTicket(userTicket);
        if (isVerify) {
            // 2. 生成临时票据：用于回跳到调用端网站，是由 CAS 端所签发的一个一次性的临时 ticket
            String tempTicket = createTempTicket();
            // 3. 直接回调会原系统
            return "redirect:" + returnUrl + "?tmpTicket=" + tempTicket;
        }

        // 用户从未登录过，第一次进入则跳转到CAS的同一登录页面
        return "login";
    }
    /**
     * 验证 全局门票是否有效
     *
     * @param userTicket
     * @return
     */
    private boolean verifyUserTicket(String userTicket) {
        // 没有全局门票，表示未登录过
        if (StringUtils.isBlank(userTicket)) {
            return false;
        }
        // 验证全局门票是否在 redis 中存在
        String userId = redisOperator.get(REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        // 验证该 userId 对应的会话是否存在
        String userJson = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(userId)) {
            return false;
        }
        return true;
    }
    /**
     * 账户注销、退出
     *
     * @param request
     * @param response
     * @return
     */
    @PostMapping("/logout")
    @ResponseBody
    public JSONResult login(HttpServletRequest request,
                            HttpServletResponse response) {

        String userTicket = getCookie(COOKIE_USER_TICKET, request);
        if (StringUtils.isBlank(userTicket)) {
            // 如果 cookie 中没有 ，则直接响应ok
            return JSONResult.ok();
        }
        String userId = redisOperator.get(REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isBlank(userId)) {
            return JSONResult.ok();
        }
        // 删除 redis 中的 key
        redisOperator.del(REDIS_USER_TICKET + ":" + userTicket);
        redisOperator.del(REDIS_USER_TOKEN + ":" + userId);
        // 删除全局门票的 cookie
        delCookie(COOKIE_USER_TICKET, response);
        return JSONResult.ok();
    }
    /**
     * 用户登录：CAS 的统一登录接口
     * <pre>
     *  目的：
     *      1. 登录后创建用户的全局会话   --> uniqueToken
     *      2. 创建用户全局门票，用于标识在 CAS 端是否登录  --> userTicket
     *      3. 创建用户的临时票据，用于回传会跳 --> tempTicket
     * </pre>
     *
     * @param username
     * @param password
     * @param returnUrl
     * @param model
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("doLogin")
    public String doLogin(String username,
                          String password,
                          String returnUrl,
                          Model model,
                          HttpServletRequest request,
                          HttpServletResponse response) throws Exception {

        // 0. 判断用户名和密码必须不为空
        if (StringUtils.isBlank(username) ||
                StringUtils.isBlank(password)) {
            model.addAttribute("errmsg", "用户名或密码不能为空");
            return "login";
        }

        // 1. 实现登录
        Users user = null;
        try {
            user = userService.queryUserForLogin(username, MD5Utils.getMD5Str(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (user == null) {
            model.addAttribute("errmsg", "用户名或密码不正确");
            return "login";
        }
        // 2. 实现用户的 redis 会话
        String uniqueToken = UUID.randomUUID().toString();
        // 永远不过期，除非自动退出
        UsersVO usersVO = new UsersVO();
        BeanUtils.copyProperties(user, usersVO);
        usersVO.setUserUniqueToken(uniqueToken);
        // redis_user_token
        // 将用户信息放到 redis 中
        redisOperator.set(REDIS_USER_TOKEN + ":" + user.getId(), JsonUtils.objectToJson(usersVO));

        // 3. 生成ticket门票，全局门票，代表用户在CAS端登录过
        String userTicket = UUID.randomUUID().toString().trim();
        // 3.1 用户全局门票需要放入CAS端的cookie中
        setCookie(COOKIE_USER_TICKET,userTicket,response);
        // 4. userTicket 关联用户id，并且放入到redis中，代表用户有门票了，可以在各个景区游玩
        redisOperator.set(REDIS_USER_TICKET + ":" + userTicket,user.getId());

        // 5. 生成临时票据，回调到调用端网站，是由 CAS 端所签发的一个一次性的临时 ticket
        String tempTicket = createTempTicket();
        /**
         * userTicket: 用于表示用户在CAS端的一个登录状态：已登录
         * tmpTicket: 用于颁发给用户进行一次性的验证票据，有时效性
         */
        /**
         *
         */
//        return "login";
        return "redirect:" + returnUrl + "?tmpTicket=" + tempTicket;
    }
    /**
     * 验证临时票据，并返回用户信息和token
     *
     * @param tmpTicket
     * @return
     */
    @PostMapping("verifyTmpTicket")
    @ResponseBody
    public JSONResult verifyTmpTicket(@RequestParam String tmpTicket, HttpServletRequest request) throws Exception {
        // 验证临时票据
        String key = REDIS_TMP_TICKET + ":" + tmpTicket;
        String tmpTicketValue = redisOperator.get(key);
        if (StringUtils.isBlank(tmpTicketValue)) {
            return JSONResult.errorUserTicket("用户票据异常");
        }

        if (!tmpTicketValue.equals(MD5Utils.getMD5Str(tmpTicket))) {
            return JSONResult.errorUserTicket("用户票据异常");
        }

        // 销毁临时票据
        redisOperator.del(key);
        /*
         这里笔者说下：它的临时票据没有和全局票均关联上
          全局票据：关联的是 用户 ID 信息
          临时票据：没有任何关联
          token：关联的是用户信息和  token，tokenKey 的组成是固定前缀+ 用户 ID
         */

        // 换取用户会话，从 cookie 中获取到全局票据，拿到用户 ID,然后拿到与 token 相关的用户信息
        String userTicket = getCookie(COOKIE_USER_TICKET, request);
        String userId = redisOperator.get(REDIS_USER_TICKET + ":" + userTicket);
        if (StringUtils.isBlank(userId)) {
            return JSONResult.errorUserTicket("用户全局票据异常");
        }
        String tokenValueJson = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
        if (StringUtils.isBlank(tokenValueJson)) {
            return JSONResult.errorUserTicket("用户 token 异常");
        }
        UsersVO usersVO = JsonUtils.jsonToPojo(tokenValueJson, UsersVO.class);
        return JSONResult.ok(usersVO);
    }
    /**
     * 创建临时票据
     * @return
     */
    private String createTempTicket() {
        String tempTicket = UUID.randomUUID().toString().trim();
        try {
            redisOperator.set(REDIS_TMP_TICKET + ":" + tempTicket, MD5Utils.getMD5Str(tempTicket),600);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tempTicket;
    }

    private void setCookie(String key, String val, HttpServletResponse response) {
//        Cookie cookie = new Cookie(key, val);
//        cookie.setDomain("sso.com");
//        cookie.setPath("/");
        ResponseCookie cookie = ResponseCookie.from(key, val) // key & value
                .secure(true)        // 在 https 下传输,配合 none 使用
                .httpOnly(false)
//                .domain("sso.com")// 域名
                .domain("localhost")// 没有域名的情况下，可以使用ip
                .path("/")            // path
                .sameSite("None") // 大多数情况也是不发送第三方 Cookie，但是导航到目标网址的 Get 请求除外 Lax 或者 none
                // .sameSite("Lax")
                .build();
        response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }
    /**
     * 删除 cookie
     *
     * @param key
     * @param response
     */
    private void delCookie(String key, HttpServletResponse response) {
        Cookie cookie = new Cookie(key, null);
        cookie.setDomain("localhost");
        cookie.setPath("/");
        cookie.setMaxAge(-1);  // 过期时间为立即过期
        response.addCookie(cookie);
    }

    /**
     * 从 request 中获取 cookie
     *
     * @param key
     * @param request
     * @return
     */
    private String getCookie(String key, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null || StringUtils.isBlank(key)) {
            return null;
        }
        for (Cookie cookie : cookies) {
            String name = cookie.getName();
            if (key.equalsIgnoreCase(name)) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
