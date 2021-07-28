package com.joker.service.impl;

import com.joker.mapper.CategoryMapper;
import com.joker.mapper.CategoryMapperCustom;
import com.joker.pojo.Category;
import com.joker.pojo.vo.CategoryVO;
import com.joker.pojo.vo.NewItemsVO;
import com.joker.service.CategoryService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryMapper categoryMapper;
    private final CategoryMapperCustom categoryMapperCustom;

    public CategoryServiceImpl(CategoryMapper categoryMapper, CategoryMapperCustom categoryMapperCustom) {
        this.categoryMapper = categoryMapper;
        this.categoryMapperCustom = categoryMapperCustom;
    }


    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<Category> queryAllRootLevelCat() {
        Example example = new Example(Category.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("type",1);
        return categoryMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<CategoryVO> getSubCatList(int rootCatId) {
        return categoryMapperCustom.getSubCatList(rootCatId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<NewItemsVO> getSixNewItemsLazy(int rootCatId) {
        Map<String,Object> map = new HashMap<>(4);
        map.put("rootCatId",rootCatId);
        return categoryMapperCustom.getSixNewItemsLazy(map);
    }
}