package org.yuan.service;

import org.yuan.pojo.Carousel;

import java.util.List;

public interface CarouselService {
    /**
     * 查询所有轮播图
     * @param isShow
     * @return
     */
    List<Carousel> queryAll(Integer isShow);
}
