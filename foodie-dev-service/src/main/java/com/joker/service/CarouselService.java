package com.joker.service;

import com.joker.pojo.Carousel;

import java.util.List;

/**
 * @version 1.0.0
 * @ClassName CarouselService.java
 * @Package com.joker.service
 * @Author Joker
 * @Description 轮播图
 * @CreateTime 2021年07月26日 15:51:00
 */
public interface CarouselService {

    /**
     * 方法描述: <br>
     * <p> 查询所有轮播图 </p>
     *
     * @Author Joker
     * @CreateDate 2021/7/26 15:55
     * @return List<Carousel>
     * @ReviseName
     * @ReviseTime 2021/7/26 15:55
     **/
    List<Carousel> queryAll();

}
