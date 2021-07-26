package com.joker.controller;

import com.joker.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * @version 1.0.0
 * @ClassName HelloController.java
 * @Package com.joker.controller
 * @Author Joker
 * @Description
 * @CreateTime 2021年07月23日 13:57:00
 */
@ApiIgnore
@RestController
public class StuFooController {

    private final StuService stuService;

    public StuFooController(StuService stuService) {
        this.stuService = stuService;
    }

    @GetMapping("/getStu")
    public Object getStu(int id){
        return stuService.getStuInfo(id);
    }
    @PostMapping("/saveStu")
    public Object saveStu(){
        stuService.saveStu();
        return "save ok";
    }
    @PostMapping("/updateStu")
    public Object updateStu(int id){
        stuService.updateStu(id);
        return "update ok";
    }
    @PostMapping("/delStu")
    public Object delStu(int id){
        stuService.deleteStu(id);
        return "delete ok";
    }
}
