package com.joker.service;

import com.joker.pojo.Stu;

/**
 * @version 1.0.0
 * @ClassName StuService.java
 * @Package com.joker.service
 * @Author Joker
 * @Description
 * @CreateTime 2021年07月23日 16:32:00
 */
public interface StuService {
    Stu getStuInfo(int id);
    void saveStu();
    void updateStud(int id);
    void deleteStu(int id);

}
