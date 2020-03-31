package org.yuan.service;

import org.yuan.pojo.*;
import org.yuan.pojo.vo.*;
import org.yuan.utils.PagedGridResult;

import java.util.List;

public interface ItemService {

   Items queryItemByID(String id);

   List<ItemsImg> queryItemImgList(String itemId);

   List<ItemsSpec> queryItemSpecList(String itemId);

   ItemsParam queryItemsParam(String itemId);

   CommentLevelCountsVO queryCommentCounts(String itemId);

   PagedGridResult queryPagedComments(String itemId, Integer level, Integer page, Integer pageSize);

   PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);

   PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize);

   List<ShopCartVO> queryItemsBySpecIds(String specIds);
}
