package org.yuan.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.yuan.enums.CommentLevel;
import org.yuan.enums.YesOrNo;
import org.yuan.mapper.*;
import org.yuan.pojo.*;
import org.yuan.pojo.vo.CommentLevelCountsVO;
import org.yuan.pojo.vo.ItemCommentVo;
import org.yuan.pojo.vo.SearchItemVO;
import org.yuan.pojo.vo.ShopCartVO;
import org.yuan.service.ItemService;
import org.yuan.utils.DesensitizationUtil;
import org.yuan.utils.PagedGridResult;
import tk.mybatis.mapper.entity.Example;

import java.lang.reflect.Array;
import java.util.*;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemByID(String id) {
        return itemsMapper.selectByPrimaryKey(id);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example example = new Example(ItemsImg.class);
        example.createCriteria()
                .andEqualTo("itemId",itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        example.createCriteria()
                .andEqualTo("itemId",itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemsParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        example.createCriteria()
                .andEqualTo("itemId",itemId);
        return itemsParamMapper.selectOneByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId){
        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts = goodCounts + normalCounts + badCounts;
        return new CommentLevelCountsVO(totalCounts,goodCounts,normalCounts,badCounts);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    private Integer getCommentCounts(String itemId,Integer level){
        ItemsComments itemsComments = new ItemsComments();
        itemsComments.setItemId(itemId);
        if(Optional.ofNullable(level).isPresent()){
            itemsComments.setCommentLevel(level);
        }
        return itemsCommentsMapper.selectCount(itemsComments);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryPagedComments(String itemId, Integer level,Integer page, Integer pageSize) {
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("itemId",itemId);
        paramsMap.put("level",level);
        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);
        List<ItemCommentVo> itemCommentVos = itemsMapperCustom.queryItemComments(paramsMap);
        itemCommentVos.stream().forEach(
                comment-> comment.setNickname(DesensitizationUtil.commonDisplay(comment.getNickname())));
        return setterPagedGrid(itemCommentVos,page);
    }

    private PagedGridResult setterPagedGrid(List<?> list,Integer page){
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("keywords",keywords);
        paramsMap.put("sort",sort);
        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);
        List<SearchItemVO> searchItemVOS = itemsMapperCustom.searchItems(paramsMap);
        return setterPagedGrid(searchItemVOS,page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItemsByThirdCat(Integer catId, String sort, Integer page, Integer pageSize) {
        Map<String,Object> paramsMap = new HashMap<>();
        paramsMap.put("catId",catId);
        paramsMap.put("sort",sort);
        /**
         * page: 第几页
         * pageSize: 每页显示条数
         */
        PageHelper.startPage(page, pageSize);
        List<SearchItemVO> searchItemVOS = itemsMapperCustom.searchItemsByThirdCat(paramsMap);
        return setterPagedGrid(searchItemVOS,page);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ShopCartVO> queryItemsBySpecIds(String specIds) {
        List<String> ids = Arrays.asList(specIds.split(","));
        return itemsMapperCustom.queryItemsBySpecIds(ids);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsSpec queryItemSpecById(String specId) {
        return itemsSpecMapper.selectByPrimaryKey(specId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public String queryItemMainImgUrlById(String itemId) {
        ItemsImg itemsImg = new ItemsImg();
        itemsImg.setItemId(itemId);
        itemsImg.setIsMain(YesOrNo.YES.type);
        ItemsImg result = itemsImgMapper.selectOne(itemsImg);
        return Optional.ofNullable(result).orElse(new ItemsImg()).getUrl();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void decreaseItemSpecStock(String specId, Integer buyCounts) {
        //TODO 需要分布式锁 控制

        int result =  itemsMapperCustom.decreaseItemSpecStock(specId,buyCounts);
        if(result != 1){
            throw new RuntimeException("订单失败，库存不足");
        }
    }


}
