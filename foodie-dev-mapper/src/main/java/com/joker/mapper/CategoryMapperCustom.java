package com.joker.mapper;


import com.joker.pojo.vo.CategoryVO;

import java.util.List;

public interface CategoryMapperCustom {
    List<CategoryVO> getSubCatList(int rootCatId);
}