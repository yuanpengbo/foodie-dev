package org.yuan.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yuan.mapper.CategoryMapper;
import org.yuan.mapper.CategoryMapperCustom;
import org.yuan.pojo.Category;
import org.yuan.pojo.vo.CategoryVO;
import org.yuan.pojo.vo.NewItemsVo;
import org.yuan.service.CategoryService;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private CategoryMapperCustom categoryMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryAllRootLevelCat() {
        Example example = new Example(Category.class);
        example.createCriteria().andEqualTo("type",1);
        return categoryMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCatList(Integer rootCatId) {
        return categoryMapperCustom.getSubCatList(rootCatId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<NewItemsVo> getSixNewItemLazy(Integer rootCatId) {
        Map<String,Object> param = new HashMap<>();
        param.put("rootCatId",rootCatId);
        return categoryMapperCustom.getSixNewItemLazy(param);
    }
}
