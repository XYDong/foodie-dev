package com.joker.pojo.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 菜单二级分类VO
 */
@Data
@ApiModel(value = "菜单二级分类",description = "菜单二级分类")
public class CategoryVO {

    @ApiModelProperty(value = "菜单id",name = "id",notes = "菜单id")
    private Integer id;
    @ApiModelProperty(value = "菜单名称",name = "name",notes = "菜单名称")
    private String name;
    @ApiModelProperty(value = "菜单类型",name = "type",notes = "菜单类型")
    private String type;
    @ApiModelProperty(value = "父菜单id",name = "fatherId",notes = "父菜单id")
    private Integer fatherId;
    @ApiModelProperty(value = "三级菜单",name = "subCategoryVOs",notes = "三级菜单列表")
    private List<SubCategoryVO> subCategoryVOs;

}
