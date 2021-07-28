package com.joker.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.joker.enums.CommentLevel;
import com.joker.mapper.*;
import com.joker.pojo.*;
import com.joker.pojo.vo.CommentLevelCountsVO;
import com.joker.pojo.vo.ItemsCommentsVO;
import com.joker.pojo.vo.SearchItemsVO;
import com.joker.pojo.vo.ShopcartVO;
import com.joker.service.ItemService;
import com.joker.utils.DesensitizationUtil;
import com.joker.utils.PagedGridResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0.0
 * @ClassName CarouselServiceImpl.java
 * @Package com.joker.service.impl
 * @Author Joker
 * @Description 轮播图接口实现
 * @CreateTime 2021年07月26日 15:56:00
 */
@Service
public class ItemServiceImpl implements ItemService {
    private final ItemsMapper itemsMapper;
    private final ItemsImgMapper itemsImgMapper;
    private final ItemsSpecMapper itemsSpecMapper;
    private final ItemsParamMapper itemsParamMapper;
    private final ItemsCommentsMapper itemsCommentsMapper;
    private final ItemsMapperCustom itemsMapperCustom;

    public ItemServiceImpl(ItemsMapper itemsMapper, ItemsImgMapper itemsImgMapper,
                           ItemsSpecMapper itemsSpecMapper, ItemsParamMapper itemsParamMapper,
                           ItemsCommentsMapper itemsCommentsMapper, ItemsMapperCustom itemsMapperCustom) {
        this.itemsMapper = itemsMapper;
        this.itemsImgMapper = itemsImgMapper;
        this.itemsSpecMapper = itemsSpecMapper;
        this.itemsParamMapper = itemsParamMapper;
        this.itemsCommentsMapper = itemsCommentsMapper;
        this.itemsMapperCustom = itemsMapperCustom;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {
        return itemsMapper.selectByPrimaryKey(itemId);
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {
        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsImgMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsSpecMapper.selectByExample(example);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("itemId",itemId);
        return itemsParamMapper.selectOneByExample(example);
    }

    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Integer goodCount = getCommentCount(itemId, CommentLevel.GOOD.type);
        Integer normalCount = getCommentCount(itemId, CommentLevel.NORMAL.type);
        Integer badCount = getCommentCount(itemId, CommentLevel.BAD.type);
        Integer totalCount = goodCount + normalCount + badCount;
        CommentLevelCountsVO commentLevelCountsVO = new CommentLevelCountsVO();
        commentLevelCountsVO.setGoodCounts(goodCount);
        commentLevelCountsVO.setNormalCounts(normalCount);
        commentLevelCountsVO.setBadCounts(badCount);
        commentLevelCountsVO.setTotalCounts(totalCount);
        return commentLevelCountsVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCount(String itemId, Integer level) {
        ItemsComments itemsComments = new ItemsComments();
        itemsComments.setItemId(itemId);
        if (level != null) {
            itemsComments.setCommentLevel(level);
        }

        return itemsCommentsMapper.selectCount(itemsComments);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryItemComments(String itemId, Integer level,Integer page, Integer pageSize) {
        PageHelper.startPage(page,pageSize);
        Map<String,Object> map = new HashMap<>(4);
        map.put("itemId",itemId);
        map.put("level",level);
        PageHelper.startPage(page,pageSize);
        List<ItemsCommentsVO> itemsCommentsVOS = itemsMapperCustom.queryItemComments(map);
        itemsCommentsVOS.forEach(var -> var.setNickname(DesensitizationUtil.commonDisplay(var.getNickname())));
        return setPagedGrid(page, itemsCommentsVOS);
    }

    private PagedGridResult setPagedGrid(Integer page, List<?> list) {
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult pagedGridResult = new PagedGridResult();
        pagedGridResult.setPage(page);
        pagedGridResult.setRows(list);
        pagedGridResult.setRecords(pageList.getTotal());
        return pagedGridResult;
    }

    @Override
    public PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize) {
        Map<String,Object> map = new HashMap<>(4);
        map.put("keywords",keywords);
        map.put("sort",sort);
        PageHelper.startPage(page,pageSize);
        List<SearchItemsVO> searchItemsVOS = itemsMapperCustom.searchItems(map);
        return setPagedGrid(page,searchItemsVOS);
    }

    @Override
    public PagedGridResult searchItemsByThirdCat(String sort, Integer catId, Integer page, Integer pageSize) {
        Map<String,Object> map = new HashMap<>(4);
        map.put("sort",sort);
        map.put("catId",catId);
        PageHelper.startPage(page,pageSize);
        List<SearchItemsVO> searchItemsVOS = itemsMapperCustom.searchItems(map);
        return setPagedGrid(page,searchItemsVOS);
    }

    @Override
    public List<ShopcartVO> queryItemsBySpecIds(String specIds) {
        String[] split = specIds.split(",");
        List<String> list = Arrays.asList(split);
        return itemsMapperCustom.queryItemsBySpecIds(list);
    }
}
