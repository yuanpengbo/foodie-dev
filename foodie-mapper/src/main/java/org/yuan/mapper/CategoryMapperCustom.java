package org.yuan.mapper;

import org.yuan.my.mapper.MyMapper;
import org.yuan.pojo.Category;
import org.yuan.pojo.vo.CategoryVO;

import java.util.List;

public interface CategoryMapperCustom {

    List<CategoryVO> getSubCatList(Integer rootCatId);
}