package com.demo.netty.proxy.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

/**
 * @author owen
 * @date 2024/8/29 15:07
 * @description
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("service_account")
public class ServiceAccount implements Serializable {

    @TableField(value = "id")
    @TableId(type = IdType.AUTO)
    private int id;

    private String username;

    private String password;
}
