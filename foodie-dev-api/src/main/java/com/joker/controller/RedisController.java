package com.joker.controller;

import com.joker.utils.RedisOperator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @version 1.0.0
 * @ClassName HelloController.java
 * @Package com.joker.controller
 * @Author Joker
 * @Description
 * @CreateTime 2021年07月23日 13:57:00
 */
@RestController
//@ApiIgnore
@RequestMapping("redis")
public class RedisController {

    private final RedisTemplate redisTemplate;
    private final RedisOperator redisOperator;

    public RedisController(RedisTemplate redisTemplate, RedisOperator redisOperator) {
        this.redisTemplate = redisTemplate;
        this.redisOperator = redisOperator;
    }

    @PostMapping("/set")
    public Object set(@RequestParam String key, @RequestParam String value){
//        redisTemplate.opsForValue().set(key,value);
        redisOperator.set(key, value);
        return "set ok";
    }
    @PostMapping("/get")
    public Object get(@RequestParam String key){
//        return (String)redisTemplate.opsForValue().get(key);
        return redisOperator.get(key);
    }
    @PostMapping("/del")
    public Object del(@RequestParam String key){
//        Boolean delete = redisTemplate.delete(key);
        redisOperator.del(key);
        return "del ok";
    }
}
