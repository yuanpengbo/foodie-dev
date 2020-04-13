package org.yuan.mapper;

import org.apache.ibatis.annotations.Param;
import org.yuan.my.mapper.MyMapper;
import org.yuan.pojo.ItemsComments;
import org.yuan.pojo.vo.MyCommentVO;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    void saveComments(Map<String, Object> map);

    List<MyCommentVO> queryMyComments(@Param("param") Map<String,Object> param);
}