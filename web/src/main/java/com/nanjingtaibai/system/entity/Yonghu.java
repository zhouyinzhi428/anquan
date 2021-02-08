package com.nanjingtaibai.system.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author zyz
 * @since 2020-12-18
 */
@Data
@TableName("yonghu")
public class Yonghu {
    private String id;
    private String username;
    private Integer userpassword;
}