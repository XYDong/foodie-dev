package com.joker.service;

import com.joker.pojo.Items;
import com.joker.pojo.ItemsImg;
import com.joker.pojo.ItemsParam;
import com.joker.pojo.ItemsSpec;
import com.joker.pojo.vo.CommentLevelCountsVO;
import com.joker.pojo.vo.ShopcartVO;
import com.joker.utils.PagedGridResult;

import java.util.List;

/**
 * @version 1.0.0
 * @ClassName CarouselService.java
 * @Package com.joker.service
 * @Author Joker
 * @Description 商品详情
 * @CreateTime 2021年07月26日 15:51:00
 */
public interface ItemService {

    /**
     * 查询商品详情
     * @param itemId 商品id
     * @return
     */
    Items queryItemById(String itemId);

    /**
     * 根据商品id查询商品图片列表
     * @param itemId 商品id
     * @return
     */
    List<ItemsImg> queryItemImgList(String itemId);

    /**
     * 根据商品id查询商品规格
     * @param itemId 商品id
     * @return
     */
    List<ItemsSpec> queryItemSpecList(String itemId);

    /**
     * 根据商品id查询商品参数
     * @param itemId
     * @return
     */
    ItemsParam queryItemParam(String itemId);

    /**
     * 方法描述: <br>
     * <p> 根据商品id查询商品评价等级数量 </p>
     *
     * @Author Joker
     * @CreateDate 2021/7/27 11:33
     * @param itemId
     * @return ItemsParam
     * @ReviseName
     * @ReviseTime 2021/7/27 11:33
     **/
    CommentLevelCountsVO queryCommentCounts(String itemId);

    /**
     * 方法描述: <br>
     * <p> 根据商品id查询商品的评价列表 </p>
     *
     * @Author Joker
     * @CreateDate 2021/7/27 14:15
     * @param itemId 商品id
     * @param level 评价等级
     * @return CommentLevelCountsVO
     * @ReviseName
     * @ReviseTime 2021/7/27 14:15
     **/
    PagedGridResult queryItemComments(String itemId, Integer level, Integer page, Integer pageSize);

    /**
     * 方法描述: <br>
     * <p>  </p>
     *
     * @Author Joker
     * @CreateDate 2021/7/28 11:09
     * @param keywords
     * @param sort
     * @param page
     * @param pageSize
     * @return PagedGridResult
     * @ReviseName
     * @ReviseTime 2021/7/28 11:09
     **/
    PagedGridResult searchItems(String keywords, String sort, Integer page, Integer pageSize);

    /**
     * 方法描述: <br>
     * <p> 通过分类id搜索商品列表 </p>
     *
     * @Author Joker
     * @CreateDate 2021/7/28 11:39
     * @param sort
     * @param catId
     * @param page
     * @param pageSize
     * @return PagedGridResult
     * @ReviseName
     * @ReviseTime 2021/7/28 11:39
     **/
    PagedGridResult searchItemsByThirdCat(String sort, Integer catId, Integer page, Integer pageSize);

    /**
     * 根据规格ids查询最新的购物车中商品数据（用于刷新渲染购物车中的商品数据）
     * @param specIds
     * @return
     */
    List<ShopcartVO> queryItemsBySpecIds(String specIds);

    /**
     * 根据商品规格id获取规格对象的具体信息
     * @param specId
     * @return
     */
    ItemsSpec queryItemSpecById(String specId);

    /**
     * 根据商品id获得商品图片主图url
     * @param itemId
     * @return
     */
    String queryItemMainImgById(String itemId);

    /**
     * 减少库存
     * @param specId
     * @param buyCounts
     */
    void decreaseItemSpecStock(String specId, int buyCounts);

}
