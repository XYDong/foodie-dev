package com.joker.mapper;


import com.joker.pojo.vo.ItemsCommentsVO;
import com.joker.pojo.vo.SearchItemsVO;
import com.joker.pojo.vo.ShopcartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {

    List<ItemsCommentsVO> queryItemComments(@Param("paramMap") Map<String,Object> paramMap);

    List<SearchItemsVO> searchItems(@Param("paramMap") Map<String,Object> paramMap);

    List<SearchItemsVO> searchItemsByThirdCat(@Param("paramMap") Map<String,Object> paramMap);

    public List<ShopcartVO> queryItemsBySpecIds(@Param("paramsList") List specIdsList);

    public int decreaseItemSpecStock(@Param("specId") String specId,
                                     @Param("pendingCounts") int pendingCounts);
}