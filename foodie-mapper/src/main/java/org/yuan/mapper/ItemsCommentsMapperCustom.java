package org.yuan.mapper;

import org.yuan.my.mapper.MyMapper;
import org.yuan.pojo.ItemsComments;

import java.util.Map;

public interface ItemsCommentsMapperCustom extends MyMapper<ItemsComments> {

    void saveComments(Map<String, Object> map);
}