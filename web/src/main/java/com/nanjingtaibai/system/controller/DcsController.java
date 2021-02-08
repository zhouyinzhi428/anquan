package com.nanjingtaibai.system.controller;


import com.baomidou.dynamic.datasource.annotation.DS;
import com.nanjingtaibai.response.Result;
import com.nanjingtaibai.system.entity.Mcgs;
import com.nanjingtaibai.system.entity.OpcItemValue;
import com.nanjingtaibai.system.mapper.DcsHisMapper;
import com.nanjingtaibai.system.mapper.McgsMapper;
import com.nanjingtaibai.system.mapper.OpcItemValueMapper;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zyz
 * @since 2020-12-18
 */
@RestController
@RequestMapping("/dcs/")
public class DcsController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private OpcItemValueMapper opcItemValueMapper;
    @Autowired
    private McgsMapper mcgsMapper;
    @Autowired
    DcsHisMapper dcsHisMapper;

    @ApiOperation(value = "获取OPC数据" ,notes = "获取OPC数据")
    @GetMapping("getOpcRunTimeList")
    public Result  getOpcRunTimeList(){
        List<OpcItemValue> opcItemValueList = opcItemValueMapper.selectList(null);
        return Result.ok().data("dcslist",opcItemValueList);
    }
    @ApiOperation(value = "根据位号获取实时曲线数据" ,notes = "根据位号获取实时曲线数据")
    @GetMapping("getRunTimeDataHisByPointName")
    @DS("slave_3")
    public Result  getRunTimeDataHisByPointName(@RequestParam(required = true) String pointName){

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("yyyyMM");
        Date currentDateTime=new Date();
        Date now_30 = new Date(currentDateTime.getTime() - 1800000);
        String tableName="his30";
//        QueryWrapper<DcsHis> queryWrapper=new QueryWrapper<>();
//        queryWrapper.eq("point_name",pointName).orderByDesc("addtime").last("limit 100");
//        MybatisPlusConfig.TABLE_NAME.set("his202101");
//        List<DcsHis> dcsHis = dcsHisMapper.selectList(queryWrapper);
        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from "+tableName+" where point_name='"+pointName+
                "' and (addtime between '"+simpleDateFormat.format(now_30)+"' and '"+simpleDateFormat.format(currentDateTime)+"' ) order by addtime asc");
        return Result.ok().data("hisData",maps);
    }

    @ApiOperation(value = "根据位号获取历史曲线数据" ,notes = "根据位号获取历史曲线数据")
    @GetMapping("getDataHisByPointName")
    @DS("slave_3")
    public Result  getDataHisByPointName(@RequestParam(required = true) String pointName,@RequestParam(required = true) String selectDate) throws ParseException {
        String tableName=null;

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("yyyyMM");
        Date currentDateTime=new Date();

        if(selectDate.equals("")){
            selectDate=simpleDateFormat.format(currentDateTime);
            tableName="his"+simpleDateFormat2.format(currentDateTime);
            System.out.println(selectDate);
        }else{
            System.out.println("haha"+simpleDateFormat2.format(simpleDateFormat.parse(selectDate))+"获取到的时间");
            tableName="his"+simpleDateFormat2.format(simpleDateFormat.parse(selectDate));
        }

        List<Map<String, Object>> maps = jdbcTemplate.queryForList("select * from "+tableName+" where point_name='"+pointName+
                "' and to_days(addtime)=to_days('"+simpleDateFormat.format(simpleDateFormat.parse(selectDate))+"') order by addtime asc");
        return Result.ok().data("hisData",maps);
    }

    @ApiOperation(value = "获取MCGS数据" ,notes = "获取MCGS数据")
    @GetMapping("getMcgsRunTimeList")
    @DS("slave_4")
    public Result  getMcgsRunTimeList(){
        Mcgs mcgs=mcgsMapper.getData();
        return Result.ok().data("mcgsdata",mcgs);
    }

    @ApiOperation(value = "历史数据插入" ,notes = "历史数据插入")
    @GetMapping("testadd")
    @DS("slave_3")
    @Scheduled(cron = "*/10 * * * * ?")
    @Async
    public void  testadd(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("yyyyMM");
        Date currentDateTime=new Date();
        String tableName="his"+simpleDateFormat2.format(currentDateTime);
        System.out.println(simpleDateFormat.format(currentDateTime));
        System.out.println(tableName);
        List<OpcItemValue> opcItemValueList=new ArrayList<>();
        opcItemValueList=opcItemValueMapper.selectList(null);
        for (OpcItemValue opcItemValue : opcItemValueList){
            String sql="insert into "+tableName+"(point_name,point_val,addtime) values('"+opcItemValue.getPointname()+"',"+opcItemValue.getFvalue()+",'"+simpleDateFormat.format(currentDateTime)+"')";
            jdbcTemplate.execute(sql);
        }
        Map<String,Float> map=getDataMap();
        System.out.println(map);
        for(Map.Entry<String, Float> entry : map.entrySet()){
            String mapKey = entry.getKey();
            Float mapValue = entry.getValue();
            String sql="insert into "+tableName+"(point_name,point_val,addtime) values('"+mapKey+"',"+mapValue+",'"+simpleDateFormat.format(currentDateTime)+"')";
            jdbcTemplate.execute(sql);
        }


    }
    @ApiOperation(value = "历史数据插入30分钟" ,notes = "历史数据插入30分钟")
    @GetMapping("testadd30")
    @DS("slave_3")
    @Scheduled(cron = "*/5 * * * * ?")
    @Async
    public void  testadd30(){
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleDateFormat2=new SimpleDateFormat("yyyyMM");
        Date currentDateTime=new Date();
        System.out.println("3秒插入一次"+simpleDateFormat.format(currentDateTime));
        List<OpcItemValue> opcItemValueList=new ArrayList<>();
        opcItemValueList=opcItemValueMapper.selectList(null);
        for (OpcItemValue opcItemValue : opcItemValueList){
            String sql="insert into his30(point_name,point_val,addtime) values('"+opcItemValue.getPointname()+"',"+opcItemValue.getFvalue()+",'"+simpleDateFormat.format(currentDateTime)+"')";
            jdbcTemplate.execute(sql);
        }
        Map<String,Float> map=getDataMap();
        for(Map.Entry<String, Float> entry : map.entrySet()){
            String mapKey = entry.getKey();
            Float mapValue = entry.getValue();
            String sql="insert into his30(point_name,point_val,addtime) values('"+mapKey+"',"+mapValue+",'"+simpleDateFormat.format(currentDateTime)+"')";
            jdbcTemplate.execute(sql);
        }

    }

    @ApiOperation(value = "删除实时数据30分钟前数据" ,notes = "删除实时数据30分钟前数据")
    @GetMapping("delete30")
    @DS("slave_3")
    @Async
    @Scheduled(cron = "0 */30 * * * ?")
    public void  delete30(){
        String sql="delete from his30 where addtime<(CURRENT_TIMESTAMP()+INTERVAL -30 MINUTE)";
        jdbcTemplate.execute(sql);
        System.out.println("我执行了删除，你看下效果");
    }

    public Map<String,Float> getDataMap(){
        Mcgs mcgs=mcgsMapper.getData();
        Map<String,Float> map=new HashMap<>();
        map.put("pv01",mcgs.getPv01());
        map.put("pv02",mcgs.getPv02());
        map.put("pv03",mcgs.getPv03());
        map.put("pv04",mcgs.getPv04());
        map.put("pv05",mcgs.getPv05());
        map.put("pv06",mcgs.getPv06());
        map.put("pv07",mcgs.getPv07());
        map.put("pv08",mcgs.getPv08());
        map.put("pv09",mcgs.getPv09());
        map.put("pv10",mcgs.getPv10());
        map.put("pv11",mcgs.getPv11());
        map.put("pv12",mcgs.getPv12());
        map.put("pv13",mcgs.getPv13());
        map.put("pv14",mcgs.getPv14());
        map.put("pv15",mcgs.getPv15());
        map.put("pv16",mcgs.getPv16());
        map.put("pv17",mcgs.getPv17());
        map.put("pv18",mcgs.getPv18());
        map.put("pv19",mcgs.getPv19());
        map.put("pv20",mcgs.getPv20());
        map.put("wpv12",mcgs.getWpv12());
        map.put("wpv15",mcgs.getWpv15());
        map.put("wpv16",mcgs.getWpv16());
        map.put("wpv18",mcgs.getWpv18());
        map.put("wpv19",mcgs.getWpv19());
        map.put("wpv2",mcgs.getWpv2());
        map.put("wpv3",mcgs.getWpv3());
        map.put("wpv7",mcgs.getWpv7());
        map.put("wpv8",mcgs.getWpv8());
        map.put("wpv9",mcgs.getWpv9());
        map.put("kypv02",mcgs.getKypv02());
        map.put("kypv03",mcgs.getKypv03());
        map.put("kypv04",mcgs.getKypv04());
        map.put("kypv05",mcgs.getKypv05());
        map.put("kypv06",mcgs.getKypv06());
        map.put("kypv07",mcgs.getKypv07());
        map.put("kypv08",mcgs.getKypv08());
        map.put("kypv09",mcgs.getKypv09());
        map.put("wpv20",mcgs.getWpv20());
        map.put("wpv32",mcgs.getWpv32());
        map.put("wpv31",mcgs.getWpv31());
        map.put("wpv30",mcgs.getWpv30());
        map.put("wpv29",mcgs.getWpv29());
        map.put("wpv28",mcgs.getWpv28());
        map.put("wpv27",mcgs.getWpv27());
        map.put("wpv23",mcgs.getWpv23());
        map.put("wpv22",mcgs.getWpv22());
        map.put("kypv01",mcgs.getKypv01());
        map.put("pv11",mcgs.getPv11());
        map.put("wpv1",mcgs.getWpv1());
        map.put("wpv26",mcgs.getPv20());
        map.put("wpv33",mcgs.getWpv33());
        map.put("wsv33",mcgs.getWsv33());
        map.put("wpv41",mcgs.getWpv41());
        map.put("wpv42",mcgs.getWpv42());
        return map;
    }




}

