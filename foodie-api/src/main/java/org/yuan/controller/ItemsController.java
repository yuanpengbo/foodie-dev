package org.yuan.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.yuan.pojo.Items;
import org.yuan.pojo.ItemsImg;
import org.yuan.pojo.ItemsParam;
import org.yuan.pojo.ItemsSpec;
import org.yuan.pojo.vo.CommentLevelCountsVO;
import org.yuan.pojo.vo.ItemInfoVo;
import org.yuan.service.ItemService;
import org.yuan.utils.JSONResult;
import org.yuan.utils.PagedGridResult;

import java.util.List;
import java.util.Optional;

@Api(value = "商品接口" ,tags ={"商品信息展示的相关接口"})
@RestController
@RequestMapping("items")
public class ItemsController extends BaseController{

    @Autowired
    private ItemService itemService;

    @ApiOperation(value = "查询商品详情",notes = "查询商品详情" ,httpMethod = "GET")
    @GetMapping("/info/{itemId}")
    public JSONResult info(
            @ApiParam(name = "itemId" ,value = "商品ID",required = true)
            @PathVariable String itemId){

        if(StringUtils.isBlank(itemId)){
            return JSONResult.errorMsg("商品ID 不能为空");
        }

        Items items = itemService.queryItemByID(itemId);
        List<ItemsImg> itemsImgList = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecList = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemsParam(itemId);
        return JSONResult.ok(new ItemInfoVo(items,itemsImgList,itemsSpecList,itemsParam));
    }

    @ApiOperation(value = "查询商品评价等级",notes = "查询商品评价等级" ,httpMethod = "GET")
    @GetMapping("/commentLevel")
    public JSONResult commentLevel(
            @ApiParam(name = "itemId" ,value = "商品ID",required = true)
            @RequestParam String itemId){

        if(StringUtils.isBlank(itemId)){
            return JSONResult.errorMsg("商品ID 不能为空");
        }
        CommentLevelCountsVO commentLevelCountsVO = itemService.queryCommentCounts(itemId);
        return JSONResult.ok(commentLevelCountsVO);
    }

    @ApiOperation(value = "查询商品品论",notes = "查询商品品论" ,httpMethod = "GET")
    @GetMapping("/comments")
    public JSONResult comments(
            @ApiParam(name = "itemId" ,value = "商品ID",required = true)
            @RequestParam String itemId,
            @ApiParam(name = "level" ,value = "商品评价等级",required = false)
            Integer level,
            @ApiParam(name = "page" ,value = "查询第几页",required = false)
            Integer page,
            @ApiParam(name = "pageSize" ,value = "分页的每一页显示条数",required = false)
            Integer pageSize){

        if(StringUtils.isBlank(itemId)){
            return JSONResult.errorMsg("商品ID 不能为空");
        }

        if(!Optional.ofNullable(page).isPresent()){
            page = 1;
        }

        if(!Optional.ofNullable(pageSize).isPresent()){
            page = COMMENT_PAGE_SIZE;
        }
        PagedGridResult pagedGridResult = itemService.queryPagedComments(itemId, level, page, pageSize);
        return JSONResult.ok(pagedGridResult);
    }
}
