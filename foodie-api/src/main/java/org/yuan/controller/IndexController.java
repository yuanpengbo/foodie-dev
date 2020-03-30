package org.yuan.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.yuan.enums.YesOrNo;
import org.yuan.pojo.Carousel;
import org.yuan.pojo.Category;
import org.yuan.pojo.vo.CategoryVO;
import org.yuan.pojo.vo.NewItemsVo;
import org.yuan.service.CarouselService;
import org.yuan.service.CategoryService;
import org.yuan.utils.JSONResult;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.Optional;


@Api(value = "首页",tags =  "首页相关接口")
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private CarouselService carouselService;

    @Autowired
    private CategoryService categoryService;


    @ApiOperation(value = "获取首页轮播图",notes = "获取首页轮播图",httpMethod = "GET")
    @GetMapping("/carousel")
    public JSONResult carousel(){
        List<Carousel> result = carouselService.queryAll(YesOrNo.YES.type);
        return JSONResult.ok(result);
    }

    @ApiOperation(value = "获取获取商品分类（一级分类）",notes = "获取获取商品分类（一级分类）",httpMethod = "GET")
    @GetMapping("/cats")
    public JSONResult cats(){
        List<Category> result = categoryService.queryAllRootLevelCat();
        return JSONResult.ok(result);
    }


    @ApiOperation(value = "获取商品子分类",notes = "获取商品子分类",httpMethod = "GET")
    @GetMapping("/subCat/{rootCatId}")
    public JSONResult subCat(
            @ApiParam(name="rootCatId",value = "一级分类ID",required = true)
            @PathVariable Integer rootCatId) {
        if(!Optional.ofNullable(rootCatId).isPresent()){
            return JSONResult.errorMsg("分类不存在");
        }
        List<CategoryVO> subCatList = categoryService.getSubCatList(rootCatId);
        return JSONResult.ok(subCatList);
    }


    @ApiOperation(value = "查询每个一级分类下的最新六条商品",notes = "查询每个一级分类下的最新六条商品",httpMethod = "GET")
    @GetMapping("/sixNewItems/{rootCatId}")
    public JSONResult sixNewItems(
            @ApiParam(name="rootCatId",value = "一级分类ID",required = true)
            @PathVariable Integer rootCatId) {
        if(!Optional.ofNullable(rootCatId).isPresent()){
            return JSONResult.errorMsg("分类不存在");
        }
        List<NewItemsVo> sixNewItemLazy = categoryService.getSixNewItemLazy(rootCatId);
        return JSONResult.ok(sixNewItemLazy);
    }
}
