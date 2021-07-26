package com.joker.pojo.bo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @version 1.0.0
 * @ClassName UserBO.java
 * @Package com.joker.pojo.bo
 * @Author Joker
 * @Description 创建用户时的传参
 * @CreateTime 2021年07月26日 10:16:00
 */
@Data
@ApiModel(value = "用户对象BO", description = "从客户端传入")
public class UserBO {

    @ApiModelProperty(value = "用户名",name = "username",example = "joker",required = true)
    private String username;
    @ApiModelProperty(value = "密码",name = "password",example = "123456",required = true)
    private String password;
    @ApiModelProperty(value = "确认密码", name = "confirmPassword",example = "123456",required = true)
    private String confirmPassword;
}
