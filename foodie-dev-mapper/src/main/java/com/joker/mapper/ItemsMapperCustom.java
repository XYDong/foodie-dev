package com.joker.mapper;


import com.joker.pojo.vo.ItemsCommentsVO;
import com.joker.pojo.vo.SearchItemsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {

    List<ItemsCommentsVO> queryItemComments(@Param("paramMap") Map<String,Object> paramMap);

    List<SearchItemsVO> searchItems(@Param("paramMap") Map<String,Object> paramMap);
}