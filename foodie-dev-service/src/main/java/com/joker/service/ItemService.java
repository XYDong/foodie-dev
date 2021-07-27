package com.joker.service;

import com.joker.pojo.*;
import com.joker.pojo.vo.CommentLevelCountsVO;
import com.joker.pojo.vo.ItemsCommentsVO;
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

}
