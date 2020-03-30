package org.yuan.service;

import org.yuan.pojo.*;
import org.yuan.pojo.vo.CategoryVO;
import org.yuan.pojo.vo.CommentLevelCountsVO;
import org.yuan.pojo.vo.ItemCommentVo;
import org.yuan.pojo.vo.NewItemsVo;
import org.yuan.utils.PagedGridResult;

import java.util.List;

public interface ItemService {

   Items queryItemByID(String id);

   List<ItemsImg> queryItemImgList(String itemId);

   List<ItemsSpec> queryItemSpecList(String itemId);

   ItemsParam queryItemsParam(String itemId);

   CommentLevelCountsVO queryCommentCounts(String itemId);

   PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);
}
