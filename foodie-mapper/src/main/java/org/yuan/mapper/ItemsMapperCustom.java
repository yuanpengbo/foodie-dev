package org.yuan.mapper;

import org.apache.ibatis.annotations.Param;
import org.yuan.my.mapper.MyMapper;
import org.yuan.pojo.Items;
import org.yuan.pojo.vo.ItemCommentVo;
import org.yuan.pojo.vo.SearchItemVO;
import org.yuan.pojo.vo.ShopCartVO;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom  {

    List<ItemCommentVo> queryItemComments(@Param("paramsMap") Map<String,Object> paramsMap);


    List<SearchItemVO> searchItems(@Param("paramsMap") Map<String,Object> paramsMap);

    List<SearchItemVO> searchItemsByThirdCat(@Param("paramsMap") Map<String,Object> paramsMap);

    List<ShopCartVO> queryItemsBySpecIds(@Param("paramsList") List<String> paramsList);



}