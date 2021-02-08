package com.nanjingtaibai.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DcsHis {
    @TableId("id")
    private int id;
    @TableField("point_name")
    private String pointname;
    @TableField("point_val")
    private Float pointval;
    @TableField("addtime")
    @JsonFormat(pattern = "yyyy-MM-dd HH-mm-ss")
    private Date addtime;
}
