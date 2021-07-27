package com.joker.mapper;


import com.joker.my.mymapper.MyMapper;
import com.joker.pojo.Items;
import com.joker.pojo.vo.ItemsCommentsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom {

    List<ItemsCommentsVO> queryItemComments(@Param("paramMap") Map<String,Object> paramMap);
}