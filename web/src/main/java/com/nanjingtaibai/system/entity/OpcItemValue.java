package com.nanjingtaibai.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("t_rd_opcitemrtvalue")
public class OpcItemValue {
    @TableId("F_ID")
    private int id;
    @TableField("F_CODE")
    private int fcode;
    @TableField("F_NAME")
    private String fname;
    @TableField("F_ITEMID")
    private String fitemid;
    @TableField("F_VALUE")
    private float fvalue;
    @TableField("F_QUALITY")
    private String fquality;
    @TableField("F_TIMESTAMP")
    private Date time;
    @TableField("POINT_NAME")
    private String pointname;
    @TableField("POINT_DESC")
    private String pointdesc;

}
