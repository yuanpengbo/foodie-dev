package org.yuan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yuan.mapper.CarouselMapper;
import org.yuan.pojo.Carousel;
import org.yuan.service.CarouselService;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    public CarouselMapper carouselMapper ;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Carousel> queryAll(Integer isShow) {
        Example example = new Example(Carousel.class);
        example.createCriteria()
                .andEqualTo("isShow",isShow);
        example.orderBy("sort").desc();
        return carouselMapper.selectByExample(example);
    }
}
