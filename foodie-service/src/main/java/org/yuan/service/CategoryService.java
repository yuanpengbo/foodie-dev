package org.yuan.service;

import org.yuan.pojo.Category;
import org.yuan.pojo.vo.CategoryVO;
import org.yuan.pojo.vo.NewItemsVo;

import java.util.List;

public interface CategoryService {

    List<Category> queryAllRootLevelCat();

    List<CategoryVO> getSubCatList(Integer rootCatId);

    List<NewItemsVo> getSixNewItemLazy(Integer rootCatId);
}
