package com.joker.service.impl;

import com.joker.mapper.StuMapper;
import com.joker.pojo.Stu;
import com.joker.service.StuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @version 1.0.0
 * @ClassName StuServiceImpl.java
 * @Package com.joker.service.impl
 * @Author Joker
 * @Description
 * @CreateTime 2021年07月23日 16:35:00
 */
@Service
public class StuServiceImpl implements StuService {
    @Autowired
    private StuMapper stuMapper;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Stu getStuInfo(int id) {
        return stuMapper.selectByPrimaryKey(id);
    }

    @Override
    public void saveStu() {

    }

    @Override
    public void updateStud(int id) {

    }

    @Override
    public void deleteStu(int id) {

    }
}
