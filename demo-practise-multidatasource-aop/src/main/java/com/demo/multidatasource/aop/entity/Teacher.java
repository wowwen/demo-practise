package com.demo.multidatasource.aop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("teacher")
@ApiModel(value = "边缘端设备算法配置信息")
public class Teacher {

    @ApiModelProperty(value = "主键id")
    @TableField(value = "id")
    @TableId(type = IdType.AUTO)
    private int id;

    @ApiModelProperty(value = "名称")
    @TableField(value = "name")
    private String name;

    @ApiModelProperty(value = "地址")
    @TableField(value = "addr")
    private String addr;

}