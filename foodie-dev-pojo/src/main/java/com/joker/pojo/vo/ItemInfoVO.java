package com.joker.pojo.vo;

import com.joker.pojo.Items;
import com.joker.pojo.ItemsImg;
import com.joker.pojo.ItemsParam;
import com.joker.pojo.ItemsSpec;
import lombok.Data;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * @version 1.0.0
 * @ClassName ItemInfoVO.java
 * @Package com.joker.pojo.vo
 * @Author Joker
 * @Description
 * @CreateTime 2021年07月27日 10:49:00
 */
@Data
public class ItemInfoVO {
    private Items item;
    private List<ItemsImg> itemImgList;
    private List<ItemsSpec> itemSpecList;
    private ItemsParam itemParams;
}
