package org.yuan.mapper;

import org.apache.ibatis.annotations.Param;
import org.yuan.my.mapper.MyMapper;
import org.yuan.pojo.Items;
import org.yuan.pojo.vo.ItemCommentVo;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom  {

    List<ItemCommentVo> queryItemComments(@Param("paramsMap") Map<String,Object> paramsMap);
}