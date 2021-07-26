package com.joker.service.impl;

import com.joker.mapper.CarouselMapper;
import com.joker.pojo.Carousel;
import com.joker.service.CarouselService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @version 1.0.0
 * @ClassName CarouselServiceImpl.java
 * @Package com.joker.service.impl
 * @Author Joker
 * @Description 轮播图接口实现
 * @CreateTime 2021年07月26日 15:56:00
 */
@Service
public class CarouselServiceImpl implements CarouselService {
    private final CarouselMapper carouselMapper;

    public CarouselServiceImpl(CarouselMapper carouselMapper) {
        this.carouselMapper = carouselMapper;
    }

    @Override
    public List<Carousel> queryAll(Integer isShow) {
        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow",isShow);
        return carouselMapper.selectByExample(example);
    }
}
