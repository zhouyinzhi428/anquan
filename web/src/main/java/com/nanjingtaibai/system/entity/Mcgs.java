package com.nanjingtaibai.system.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@TableName("shuju_MCGS")
public class Mcgs {
    @TableField("MCGS_Time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date mcgstime;
    private float pv01;
    private float pv02;
    private float pv03;
    private float pv04;
    private float pv05;
    private float pv06;
    private float pv07;
    private float pv08;
    private float pv09;
    private float pv10;
    private float pv12;
    private float pv13;
    private float pv14;
    private float pv15;
    private float pv16;
    private float pv17;
    private float pv18;
    private float pv19;
    private float pv20;
    private float pv21;
    private float wpv12;
    private float wpv15;
    private float wpv16;
    private float wpv18;
    private float wpv19;
    private float wpv2;
    private float wpv3;
    private float wpv7;
    private float wpv8;
    private float wpv9;
    private float kypv02;
    private float kypv03;
    private float kypv04;
    private float kypv05;
    private float kypv06;
    private float kypv07;
    private float kypv08;
    private float kypv09;
    private float wpv20;
    private float wpv32;
    private float wpv31;
    private float wpv30;
    private float wpv29;
    private float wpv28;
    private float wpv27;
    private float wpv23;
    private float wpv22;
    private float kypv01;
    private float pv11;
    private float wpv1;
    private float wpv26;
    private float wpv33;
    private float wsv33;
    private float wpv41;
    private float wpv42;

}
