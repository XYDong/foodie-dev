package com.joker.service;

import com.joker.pojo.Category;
import com.joker.pojo.vo.CategoryVO;

import java.util.List;

/**
 * 商品分类
 */
public interface CategoryService {

    /**
     * 查询所有一级分类
     * @return List<Category>
     */
    List<Category> queryAllRootLevelCat();

    /**
     * 查询子分类列表
     * @param rootCatId
     * @return
     */
    List<CategoryVO> getSubCatList(int rootCatId);
}
