package com.joker.service.impl;

import com.joker.mapper.CarouselMapper;
import com.joker.pojo.Carousel;
import com.joker.service.CarouselService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public List<Carousel> queryAll() {
        Example example = new Example(Carousel.class);
        example.orderBy("sort").desc();
        return carouselMapper.selectByExample(example);
    }
}
