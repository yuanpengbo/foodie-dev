package org.yuan.mapper;

import org.apache.ibatis.annotations.Param;
import org.yuan.my.mapper.MyMapper;
import org.yuan.pojo.Category;
import org.yuan.pojo.vo.CategoryVO;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom {

    List<CategoryVO> getSubCatList(Integer rootCatId);

    List getSixNewItemLazy(@Param("paramMap") Map<String ,Object> map);
}