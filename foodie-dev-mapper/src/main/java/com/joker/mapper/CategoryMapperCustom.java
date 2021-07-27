package com.joker.mapper;


import com.joker.pojo.vo.CategoryVO;
import com.joker.pojo.vo.NewItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CategoryMapperCustom {

    List<CategoryVO> getSubCatList(Integer rootCatId);

    List<NewItemsVO> getSixNewItemsLazy(@Param("paramMap") Map<String,Object> map);
}